
(function() {
	var app = angular.module('tilofy', [ 'ngRoute', 'ui.bootstrap' ]);

	/**
	 * PhotoController
	 */
	app.controller('BodyController', [ '$log', '$scope','$http',function($log,  $scope,  $http) {

		$http.get('/queue').success(function(datax) {
			console.log(' queue success result  ===== '+ JSON.stringify(datax));
			$scope.jobs =datax.result;
		});
				
	} ]);

	app.controller('PhotoController', function($scope, $http) {
	 
		$scope.imageUrl= 'https://scontent.cdninstagram.com/hphotos-xpa1/t51.2885-15/s640x640/sh0.08/e35/12256894_720156524782163_1349930470_n.jpg';
	    $scope.list = [];
	    $scope.formErrors = null;
	    $scope.imageWidth = 200;
	    $scope.imageHeight = 300;
	    $scope.submit = function() {	    	  
	  	    $scope.formErrors = null;
	
			if ($scope.imageWidth == undefined) {			
				console.log('no imageWidth');
				$scope.formErrors = [];
				$scope.formErrors.push ('width not set');
			}
			if ($scope.imageHeight == undefined) {
				if ($scope.formErrors == undefined){
					$scope.formErrors = [];
				}
				$scope.formErrors.push ('height not set');
			}
			if ($scope.imageUrl == undefined) {
				if ($scope.formErrors == undefined){
					$scope.formErrors = [];
				}
				$scope.formErrors.push ('URL not set');
			}
			
			if ($scope.formErrors == undefined) {
				console.log(' jpobs  ===== '+ JSON.stringify($scope.jobs));
	
				$http({
					  method: 'POST',
					  url: '/queue',
					  params: {url: $scope.imageUrl, size: ''+ $scope.imageWidth +'x'+$scope.imageHeight}
					  
					}).then(function successCallback(data) {
	
						console.log(' successCallback result  ===== '+ JSON.stringify(data));
						if ( data.data.success ) {
							console.log('  vsuccess ');
							
							console.log(' datajobs  result jobs ===== '+ JSON.stringify(data.data.jobs.result));
							$scope.jobs =data.data.jobs.result;
	
						} else {
							$scope.formErrors =data.data.errors;
							console.log('  formErrors ' + data.data.errors);
	
						}
						
	
					  }, function errorCallback(data) {
							console.log('TODO- errorCallback response' + JSON.stringify(data));
						  });
				}
				
		      };
	});

})();
