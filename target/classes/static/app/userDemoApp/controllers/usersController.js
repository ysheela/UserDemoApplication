(function () {

    var injectParams = ['$location', '$filter', '$window','userDataService', 'modalService'];

    var UsersController = function ($location, $filter, $window,userDataService, modalService) {

        var vm = this;

        vm.users = [];
        vm.filteredUsers = [];
        vm.filteredCount = 0;
        vm.orderby = 'lastName';
        vm.reverse = false;
        vm.searchText = null;
        vm.cardAnimationClass = '.card-animation';

        //paging
        vm.totalRecords = 0;
        vm.totalPages = 0;
        vm.pageSize = 10;
        vm.currentPage = 1;

        vm.pageChanged = function (page) {
            vm.currentPage = page;
            getUsersSummary();
        };

        vm.deleteUser = function (id) {
            var user = getUserById(id);
            var userName = user.firstName + ' ' + user.lastName;

            var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete User',
                headerText: 'Delete ' + userName + '?',
                bodyText: 'Are you sure you want to delete this User?'
            };

            modalService.showModal({}, modalOptions).then(function (result) {
                if (result === 'ok') {
                    userDataService.deleteUser(id).then(function () {
                        for (var i = 0; i < vm.users.length; i++) {
                            if (vm.users[i].id === id) {
                                vm.users.splice(i, 1);
                                break;
                            }
                        }
                        filterUsers(vm.searchText);
                    }, function (error) {
                        $window.alert('Error deleting user: ' + error.message);
                    });
                }
            });
        };

        vm.DisplayModeEnum = {
            Card: 0,
            List: 1
        };

        vm.changeDisplayMode = function (displayMode) {
            switch (displayMode) {
                case vm.DisplayModeEnum.Card:
                    vm.listDisplayModeEnabled = false;
                    break;
                case vm.DisplayModeEnum.List:
                    vm.listDisplayModeEnabled = true;
                    break;
            }
        };

        vm.navigate = function (url) {
            $location.path(url);
        };

        vm.setOrder = function (orderby) {
            if (orderby === vm.orderby) {
                vm.reverse = !vm.reverse;
            }
            vm.orderby = orderby;
        };

        vm.searchTextChanged = function () {
            filterUsers(vm.searchText);
        };

        function init() {
             getUsersSummary();
        }

        function getUsersSummary() {
            userDataService.getUsers(vm.currentPage - 1, vm.pageSize)
                .then(function (data) {
                    vm.totalRecords = data.totalRecords;
                    vm.totalPages = data.totalPages;
                    vm.users = data.results;
                    filterUsers(vm.searchText); //Trigger initial filter

                }, function (error) {
                    $window.alert('Sorry, an error occurred: ' + error.data.message);
                });
        }

        function filterUsers(filterText) {
            vm.filteredUsers = $filter("nameAddressFilter")(vm.users, filterText);
            vm.filteredCount = vm.filteredUsers.length;
        }

        function getUserById(id) {
            for (var i = 0; i < vm.users.length; i++) {
                var user = vm.users[i];
                if (user.id === id) {
                    return user;
                }
            }
            return null;
        }

        init();
    };

    UsersController.$inject = injectParams;

    angular.module('userDemoApp').controller('UsersController', UsersController);

}());
