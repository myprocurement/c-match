'use strict';

/* Services */

cmatchApp.factory('Account', function($resource){
        return $resource('app/rest/account', {}, {
        });
    });

cmatchApp.factory('AuthenticationSharedService', function($rootScope) {
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
