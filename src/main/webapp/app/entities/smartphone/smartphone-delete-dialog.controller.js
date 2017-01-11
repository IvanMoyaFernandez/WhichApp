(function() {
    'use strict';

    angular
        .module('whichApp')
        .controller('SmartphoneDeleteController',SmartphoneDeleteController);

    SmartphoneDeleteController.$inject = ['$uibModalInstance', 'entity', 'Smartphone'];

    function SmartphoneDeleteController($uibModalInstance, entity, Smartphone) {
        var vm = this;

        vm.smartphone = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Smartphone.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
