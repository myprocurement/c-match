'use strict';

/* Services */

cboostApp.factory('Account', function($resource){
        return $resource('app/rest/account', {}, {
        });
    });

cboostApp.factory('Password', function($resource){
    return $resource('app/rest/account/change_password', {}, {
    });
});

cboostApp.factory('Sessions', function($resource){
    return $resource('app/rest/account/sessions/:series', {}, {
        'get': { method: 'GET', isArray: true}
    });
});

cboostApp.factory('Metrics', function($resource){
    return $resource('/metrics/metrics', {}, {
        'get': { method: 'GET'}
    });
});


cboostApp.factory('AuthenticationSharedService', function($rootScope) {
    var authenticationSharedService = {};

    authenticationSharedService.message = '';

    authenticationSharedService.prepForBroadcast = function(msg) {
        this.message = msg;
        this.broadcastItem();
    };

    authenticationSharedService.broadcastItem = function() {
        $rootScope.$broadcast("authenticationEvent");
    };

    return authenticationSharedService;
});
