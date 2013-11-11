'use strict';

/* App Module */

var cboostApp = angular.module('cboostApp', ['ngResource']);

cboostApp
    .config(function ($routeProvider) {
        $routeProvider
            .when('/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginController'
            })
            .when('/settings', {
                templateUrl: 'views/settings.html',
                controller: 'SettingsController'
            })
            .when('/password', {
                templateUrl: 'views/password.html',
                controller: 'PasswordController'
            })
            .when('/sessions', {
                templateUrl: 'views/sessions.html',
                controller: 'SessionsController'
            })
            .when('/metrics', {
                templateUrl: 'views/metrics.html',
                controller: 'MetricsController'
            })
            .when('/logout', {
                templateUrl: 'views/main.html',
                controller: 'LogoutController'
            })
            .when('/mapping/:projectId', {
                templateUrl: 'views/mapping.html',
                controller: 'MappingController'
            })
            .otherwise({
                templateUrl: 'views/main.html',
                controller: 'MainController'
            })

    });
