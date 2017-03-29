(function() {
    'use strict';

    angular
        .module('whichApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('smartphone', {
            parent: 'entity',
            url: '/smartphone',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whichApp.smartphone.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/smartphone/smartphones.html',
                    controller: 'SmartphoneController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('smartphone');
                    $translatePartialLoader.addPart('enumMarca');
                    $translatePartialLoader.addPart('enumOS');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('smartphone-detail', {
            parent: 'entity',
            url: '/smartphone/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'whichApp.smartphone.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/smartphone/smartphone-detail.html',
                    controller: 'SmartphoneDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('smartphone');
                    $translatePartialLoader.addPart('enumMarca');
                    $translatePartialLoader.addPart('enumOS');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Smartphone', function($stateParams, Smartphone) {
                    return Smartphone.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'smartphone',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('smartphone-detail.edit', {
            parent: 'smartphone-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/smartphone/smartphone-dialog.html',
                    controller: 'SmartphoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Smartphone', function(Smartphone) {
                            return Smartphone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('smartphone.new', {
            parent: 'smartphone',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/smartphone/smartphone-dialog.html',
                    controller: 'SmartphoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                marca: null,
                                modelo: null,
                                camara: null,
                                frontCamara: null,
                                bateria: null,
                                pulgadasPantalla: null,
                                resolucion_pantalla_alto: null,
                                resolucionPantallaAncho: null,
                                ram: null,
                                so: null,
                                rom: null,
                                proteccionPolvo: null,
                                proteccionLiquido: null,
                                puntuacion: null,
                                descripcion: null,
                                fecha_lanzamiento: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('smartphone', null, { reload: 'smartphone' });
                }, function() {
                    $state.go('smartphone');
                });
            }]
        })
        .state('smartphone.edit', {
            parent: 'smartphone',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/smartphone/smartphone-dialog.html',
                    controller: 'SmartphoneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Smartphone', function(Smartphone) {
                            return Smartphone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('smartphone', null, { reload: 'smartphone' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('smartphone.delete', {
            parent: 'smartphone',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/smartphone/smartphone-delete-dialog.html',
                    controller: 'SmartphoneDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Smartphone', function(Smartphone) {
                            return Smartphone.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('smartphone', null, { reload: 'smartphone' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
