(function() {
    'use strict';

    angular
        .module('whichApp')
        .controller('SmartphoneController', SmartphoneController);

    SmartphoneController.$inject = ['$scope', '$state', 'Smartphone'];

    function SmartphoneController ($scope, $state, Smartphone) {
        var vm = this;

        vm.smartphones = [];

        loadAll();

        function loadAll() {
            Smartphone.query(function(result) {
                vm.smartphones = result;
                vm.searchQuery = null;
            });
        }
    }
})();
