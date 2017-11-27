(function () {

    var injectParams = ['$http', '$q'];

    var userDataService = function ($http, $q) {
        var serviceBase = '/user/',
            factory = {};

        factory.getUsers = function (pageIndex, pageSize) {
            return getPagedResource('all', pageIndex, pageSize);
        };

        factory.getUserDetails = function (pageIndex, pageSize) {
            return getPagedResource('userSummary', pageIndex, pageSize);
        };

        factory.checkUniqueValue = function (value) {
            return $http.get(serviceBase + 'email/' + escape(value)).then(
                function (results) {
                    return results.data.status;
                });
        };

        factory.insertUser = function (user) {
            user.id = null;
            return $http.put(serviceBase + '/', user).then(function (results) {
                user.id = results.data.id;
                return results.data;
            });
        };

        factory.newUser= function () {
            return $q.when({id: 0});
        };

        factory.updateUser = function (User) {
            return $http.post(serviceBase, User).then(function (status) {
                return status.data;
            });
        };

        factory.deleteUser = function (id) {
            return $http.delete(serviceBase +  id).then(function (status) {
                return status.data;
            });
        };

        factory.getUser = function (id) {
            //then does not unwrap data so must go through .data property
            //success unwraps data automatically (no need to call .data property)
            return $http.get(serviceBase +  id).then(function (results) {
                return results.data;
            });
        };

        function getPagedResource(baseResource, pageIndex, pageSize) {
            var resource = baseResource;
            resource += (arguments.length == 3) ? buildPagingUri(pageIndex, pageSize) : '';
            return $http.get(serviceBase + resource).then(function (response) {
                var users = response.data.content;
                return {
                    totalRecords: parseInt(response.data.totalElements),
                    totalPages: parseInt(response.data.totalPages),
                    results: users
                };
            });
        }

        function buildPagingUri(pageIndex, pageSize) {
            //var uri = '?$top=' + pageSize + '&$skip=' + (pageIndex * pageSize);
            var uri = '?page=' + pageIndex + '&size=' + (pageSize) + '&sort=lastName';
            return uri;
        }

        return factory;
    };

    userDataService.$inject = injectParams;

    angular.module('userDemoApp').factory('userDataService', userDataService);

}());