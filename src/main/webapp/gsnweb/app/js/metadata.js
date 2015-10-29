var metadata = angular.module("metadata", [])

        .controller("MetadataController", ["$scope", '$location', '$window', 'FilterParameters', 'difMetadata',
            function ($scope, $location, $window, FilterParameters, difMetadata) {

                $scope.hasDif = function () {
                    return $scope.hasMetadata() && difMetadata.data.dif[0].length > 0;
                };

                $scope.hasMetadata = function () {
                    return typeof difMetadata.data != 'undefined'
                };

                $scope.hasGSNMetadata = function () {
                    return $scope.hasMetadata() && difMetadata.data.gsn[0].indexOf("sensorName");
                };

                $scope.hasWikiLink = function () {
                    return $scope.gsnMetadata.features[0].properties.wikiLink != undefined;
                };

                if ($scope.hasDif()) {
                    $scope.dif = JSON.parse(difMetadata.data.dif);
                }

                if ($scope.hasGSNMetadata()) {
                    $scope.gsnMetadata = JSON.parse(difMetadata.data.gsn);
                }

                if (!$scope.hasDif() && !$scope.hasGSNMetadata()) {
                    $scope.errorMessage = difMetadata;
                }

                $scope.download = function () {
                    var url = "http://montblanc.slf.ch:22001/multidata?download_format=csv&field[0]=All&vs[0]="
                        + $scope.gsnMetadata.features[0].properties.sensorName;
                    $window.location.href = url;
                };

                $scope.explore = function () {
                    console.log('PLOT ' + $scope.gsnMetadata.features[0].properties.sensorName);
                    FilterParameters.reset();
                    FilterParameters.sensors = [$scope.gsnMetadata.features[0].properties.sensorName];
                    //FilterParameters.fields = getParametersForLink(feature)
                    FilterParameters.resetPromise();
                    $location.path('/plot')
                    FilterParameters.updateURLFromMap($location);

                };

            }])

        .factory('DifMetadataLoad', ['$http', '$q', '$route', 'FilterParameters',
            function ($http, $q, $route, FilterParameters) {

                this.promise;

                var self = this;
                var sdo = {
                    getData: function () {

                        //if (!self.promise) {
                        var sensorName = $route.current.params.sensor;

                        if (sensorName == null) {
                            if (FilterParameters.sensors.length > 0) {
                                sensorName = FilterParameters.sensors[0];
                            } else {
                                return "Please specify sensor name in URL! For example: metadata?sensor=wfj_vf_meteo";
                            }
                        }

                        var url = 'http://montblanc.slf.ch:8090/web/metadatadif/' + sensorName;

                        self.promise = $http({
                            method: 'GET',
                            url: url
                        });
                        self.promise.then(function (data) {
                            return data.response;
                        }, function (data) {
                            console.log('ERROR ' + data.statusText);
                            return 'Error when loading metadata';
                        });
                        //}
                        return self.promise;
                    }
                };
                return sdo;
            }])

        .factory('DifMetadata', function () {
            function DifMetadata(metadata) {
                this.metadata = metadata;
            }

            DifMetadata.prototype = {


                getProperties: function () {
                    return this.metadata.features[0].properties['allProperties'];
                },

                getSensorName: function () {
                    return this.metadata.features[0].properties.sensorName;
                },

                getFromDate: function () {
                    //return new Date('2001-01-01');
                    return this.metadata.features[0].properties['fromDate'];
                },

                getToDate: function () {
                    //return new Date('2016-01-01');
                    return this.metadata.features[0].properties['untilDate'];
                }


            }

            return DifMetadata;
        })
    ;
