cboostApp.factory('Project', function($resource){
    return $resource('/app/rest/project/:id',{}, {
        'updateMapping': { method: 'POST'}
    });
});

cboostApp.controller('MappingController', function MappingController($routeParams, $scope, Project, $filter) {
    $scope.error = {
        multiField :false
    }
    $scope.init = function () {
        $scope.data = Project.get({id: $routeParams.projectId}, function(data) {
            $scope.getFilePreviewSkipFirst = function(field){
                var lines = $scope.data.filePreview.slice(0);
                lines.shift();
                return lines;
            }
        });

        $scope.getFieldMatch = function(field){
            for(var i in $scope.data.matchingFieldType){
                if($scope.data.matchingFieldType[i] == field.name){
                    return i;
                }
            }
            return null;
        };
    };
    $scope.sendMapping = function(){
        Project.updateMapping({id: $routeParams.projectId, fields: $scope.data.matchingFieldType});
        console.log($scope.data.matchingFieldType);
        //$location.path('/someNewPath');
    };

    $scope.hasError = function () {
        for(error in $scope.error){
            if ($scope.error[error]){
                return true;
            }
        }
        return false ;
    }
    $scope.checkFieldError = function(){
        $scope.error.multiField = false;
        $.each($scope.data.matchingFieldType, function (i, val) {
            if(val != null && $scope.data.matchingFieldType.filter(function(x){return x==val}).length>1) {
                $scope.error.multiField = true;
            }
        });
    }

});
