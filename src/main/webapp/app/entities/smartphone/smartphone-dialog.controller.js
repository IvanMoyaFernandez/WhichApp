(function() {
    'use strict';

    angular
        .module('whichApp')
        .controller('SmartphoneDialogController', SmartphoneDialogController);

    SmartphoneDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Smartphone'];

    function SmartphoneDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Smartphone) {
        var vm = this;

        vm.smartphone = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.smartphone.id !== null) {
                Smartphone.update(vm.smartphone, onSaveSuccess, onSaveError);
            } else {
                Smartphone.save(vm.smartphone, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('whichApp:smartphoneUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fecha_lanzamiento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
