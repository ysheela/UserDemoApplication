(function() {
    var app = angular.module('userDemoApp', ['ngRoute', 'ngAnimate', 'ui.bootstrap']);

    app.config(['$routeProvider', function ($routeProvider) {
        var viewBase = '/app/userDemoApp/views/';

        $routeProvider
            .when('/users', {
                controller: 'UsersController',
                templateUrl: viewBase + 'users.html',
                controllerAs: 'vm'
            })
            .when('/useredit/:userId', {
                controller: 'UserEditController',
                templateUrl: viewBase + 'userEdit.html',
                controllerAs: 'vm',
            })
            .when('/about', {
                controller: 'AboutController',
                templateUrl: viewBase + 'about.html',
                controllerAs: 'vm'
            })
            .otherwise({ redirectTo: '/users' });

    }]);
}());