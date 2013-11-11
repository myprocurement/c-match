cboostApp.factory('Project', function($resource){
    return $resource('/app/rest/project/:id',{}, {
        'updateMapping': { method: 'POST', isArray: true}
    });
});

cboostApp.controller('MappingController', function MappingController($routeParams, $scope, Project, $filter) {
    console.log($routeParams.projectId)
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
        console.log($scope.field);
    };

});
