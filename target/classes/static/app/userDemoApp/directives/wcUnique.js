(function () {

    var injectParams = ['$q', 'userDataService'];

    var wcUniqueDirective = function ($q, userDataService) {

        var link = function (scope, element, attrs, ngModel) {
            ngModel.$asyncValidators.unique = function (modelValue, viewValue) {
                var deferred = $q.defer(),
                    currentValue = modelValue || viewValue,
                    key = attrs.wcUniqueKey,
                    property = attrs.wcUniqueProperty;

                //First time the asyncValidators function is loaded the
                //key won't be set  so ensure that we have
                //key and propertyName before checking with the server
                return $q.when(true);
            };
        };

        return {
            restrict: 'A',
            require: 'ngModel',
            link: link
        };
    };

    wcUniqueDirective.$inject = injectParams;

    angular.module('userDemoApp').directive('wcUnique', wcUniqueDirective);

}());