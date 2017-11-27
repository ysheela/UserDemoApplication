angular
    .module("userDemoApp")
    .controller("navbarController", function($scope, $location) {
        $scope.appTitle = 'User Management';
        $scope.isCollapsed = false;
        $scope.highlight = function (path) {
            return $location.path().substr(0, path.length) === path;
        };
    });
