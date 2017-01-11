(function() {
    'use strict';

    angular
        .module('whichApp')
        .controller('SmartphoneDetailController', SmartphoneDetailController);

    SmartphoneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Smartphone'];

    function SmartphoneDetailController($scope, $rootScope, $stateParams, previousState, entity, Smartphone) {
        var vm = this;

        vm.smartphone = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('whichApp:smartphoneUpdate', function(event, result) {
            vm.smartphone = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
