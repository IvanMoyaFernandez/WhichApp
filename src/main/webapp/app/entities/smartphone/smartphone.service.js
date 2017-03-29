(function() {
    'use strict';
    angular
        .module('whichApp')
        .factory('Smartphone', Smartphone);

    Smartphone.$inject = ['$resource', 'DateUtils'];

    function Smartphone ($resource, DateUtils) {
        var resourceUrl =  'api/smartphones/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha_lanzamiento = DateUtils.convertLocalDateFromServer(data.fecha_lanzamiento);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha_lanzamiento = DateUtils.convertLocalDateToServer(copy.fecha_lanzamiento);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha_lanzamiento = DateUtils.convertLocalDateToServer(copy.fecha_lanzamiento);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
