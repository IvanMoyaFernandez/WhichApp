(function() {
    'use strict';
    angular
        .module('whichApp')
        .factory('Smartphone', Smartphone);

    Smartphone.$inject = ['$resource'];

    function Smartphone ($resource) {
        var resourceUrl =  'api/smartphones/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
