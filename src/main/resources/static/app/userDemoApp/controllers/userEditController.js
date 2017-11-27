(function () {

    var injectParams = ['$scope', '$location', '$routeParams',
        '$timeout', 'userDataService', 'modalService'];

    var UserEditController = function ($scope, $location, $routeParams,
                                           $timeout,  userDataService, modalService) {

        var vm = this,
            userId = ($routeParams.userId),
            timer,
            onRouteChangeOff;

        vm.user = {};
        vm.title = (userId == -1) ? 'Add' : 'Edit';
        vm.buttonText = (userId == -1) ? 'Add' : 'Update';
        vm.updateStatus = false;
        vm.errorStatus = false;
        vm.errorMessage = '';

        vm.saveUser = function () {
            if ($scope.editForm.$valid) {
                if (!vm.user.id) {
                    userDataService.insertUser(vm.user).then(processSuccess, processError);
                }
                else {
                    userDataService.updateUser(vm.user).then(processSuccess, processError);
                }
            }
        };

        vm.deleteUser = function () {
            var userName = vm.user.firstName + ' ' + vm.user.lastName;
            var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete User',
                headerText: 'Delete ' + userName + '?',
                bodyText: 'Are you sure you want to delete this user?'
            };

            modalService.showModal({}, modalOptions).then(function (result) {
                if (result === 'ok') {
                    userDataService.deleteUser(vm.user.id).then(function () {
                        onRouteChangeOff(); //Stop listening for location changes
                        $location.path('/users');
                    }, processError);
                }
            });
        };

        function init() {

            if (userId == -1) {
                userDataService.newUser().then(function (user) {
                    vm.user = user;
                });
            } else {


                userDataService.getUser(userId).then(function (user) {
                    vm.user = user;
                }, processError);
            }


            //Make sure they're warned if they made a change but didn't save it
            //Call to $on returns a "deregistration" function that can be called to
            //remove the listener (see routeChange() for an example of using it)
            onRouteChangeOff = $scope.$on('$locationChangeStart', routeChange);
        }

        init();

        function routeChange(event, newUrl, oldUrl) {
            //Navigate to newUrl if the form isn't dirty
            if (!vm.editForm || !vm.editForm.$dirty) return;

            var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Ignore Changes',
                headerText: 'Unsaved Changes',
                bodyText: 'You have unsaved changes. Leave the page?'
            };

            modalService.showModal({}, modalOptions).then(function (result) {
                if (result === 'ok') {
                    onRouteChangeOff(); //Stop listening for location changes
                    $location.path($location.url(newUrl).hash()); //Go to page they're interested in
                }
            });

            //prevent navigation by default since we'll handle it
            //once the user selects a dialog option
            event.preventDefault();
            return;
        }

        function processSuccess() {
            $scope.editForm.$dirty = false;
            vm.updateStatus = true;
            vm.title = 'Edit';
            vm.buttonText = 'Update';
            //startTimer();
        }

        function processError(error) {
            vm.updateStatus = false;
            vm.errorStatus = true;
            vm.errorMessage = error.data.message;
            //startTimer();
        }

        function startTimer() {
            timer = $timeout(function () {
                $timeout.cancel(timer);
                //vm.errorMessage = '';
                vm.updateStatus = false;
            }, 3000);
        }
    };

    UserEditController.$inject = injectParams;

    angular.module('userDemoApp').controller('UserEditController', UserEditController);

}());