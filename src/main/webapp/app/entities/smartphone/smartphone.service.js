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
                        data.fecha_lanzamiento = DateUtils.convertDateTimeFromServer(data.fecha_lanzamiento);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
