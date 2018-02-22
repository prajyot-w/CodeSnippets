/**
 * HTML code to activate geoMap is : <div class="map" geo-map geo-json="geographyJson"></div>
 * JSON FORMAT : geographyJson = {shp: {}, data: [], legend: ''}
 * In shp all geography geoJson shape file is loaded
 * In data, geography related data is loaded
 * Legend is the name of data against geography loaded
 */
angular.module('angular-geo-map', []).directive('geoMap', function(){
    return {
        link: function(scope, element, attrs){

            var myMap = undefined;
            var legend = undefined;
            var info = undefined;
            var geoJson = undefined;

            var diffDivider = undefined;
            var fillColorArray = [
                '#FFEDA0',
                '#FED976',
                '#FEB24C',
                '#FD8D3C',
                '#FC4E2A',
                '#E31A1C',
                '#BD0026',
                '#800026'
            ];

            // Divide data into categories
            var loadDiffDivider = function(data){
                var min = data[0].value, max = data[0].value;
                for(i=0; i < data.length; i++){
                    if(data[i].value > max) max = data[i].value;
                    if(data[i].value < min) min = data[i].value;
                }

                diffDivider = Math.floor((max-min)/8);
            };

            // Style method
            var getColor = function(d){
                return  d > (diffDivider*7) ? fillColorArray[7] :
                    d > (diffDivider*6) ? fillColorArray[6] :
                        d > (diffDivider*5) ? fillColorArray[5] :
                            d > (diffDivider*4) ? fillColorArray[4] :
                                d > (diffDivider*3) ? fillColorArray[3] :
                                    d > (diffDivider*2) ? fillColorArray[2] :
                                        d > (diffDivider*1) ? fillColorArray[1] :
                                            fillColorArray[0] ;
            };

            // Style method
            var style = function(feature){
                return {
                    fillColor: getColor(feature.properties.value),
                    weight: 2,
                    opacity: 1,
                    color: 'white',
                    dashArray: '3',
                    fillOpacity: 0.7
                };
            };

            // MouseOver and MouseOut event handler
            var onEachFeature = function(feature, layer){
                layer.on(
                    {
                        mouseover: function(e){
                            info = L.control();
                            info.onAdd = function(map){
                                this._div = L.DomUtil.create('div');
                                // Styleing div
                                this._div.style.backgroundColor = 'white';
                                this._div.style.padding = '1em';
                                this._div.style.borderRadius = '0.5em';

                                this.update();
                                return this._div;
                            };
                            info.update = function(prop){
                                if(angular.isDefined(prop))
                                    this._div.innerHTML = '<h4>'+prop.name+'</h4><p><b>'+prop.value+'</b></p>';
                            };
                            info.addTo(myMap);

                            var layer = e.target;
                            layer.setStyle({
                                weight: 5,
                                color: '#ffffff',
                                dashArray: '',
                                fillOpacity: 0.7
                            });
                            info.update(layer.feature.properties);
                        },
                        mouseout: function(e){
                            geoJson.resetStyle(e.target);
                            info.update();

                            if(angular.isDefined(info)) myMap.removeControl(info);
                        }
                    }
                );
            };

            // Data load
            var populateStateData = function(dataJson){
                var geoJson = angular.copy(evalGeoJson().shp.state);
                for(i=0; i < geoJson.features.length; i++){
                    for(j=0; j < dataJson.length; j++){
                        if(dataJson[j].geography == geoJson.features[i].properties.name){
                            geoJson.features[i].properties.value = dataJson[j].value;
                            break;
                        }
                    }
                }

                return geoJson;
            };

            // Data load
            var loadData = function(dataJson){
                myMap.eachLayer(function(layer){
                    if(layer.options.level != 'base') myMap.removeLayer(layer);
                });

                if(!angular.isUndefined(dataJson)){
                    loadDiffDivider(dataJson);

                    if(angular.isDefined(legend)) myMap.removeControl(legend);

                    // Loading legend
                    legend = L.control({position: 'bottomright'});
                    legend.onAdd = function(myMap) {
                        var div = L.DomUtil.create('div');

                        // Styleing div
                        div.style.backgroundColor = 'white';
                        div.style.padding = '1em';
                        div.style.borderRadius = '0.5em';

                        var label = [];
                        for(i=(fillColorArray.length); i > 0; i--){
                            label.push("<li><i style='padding: 0.3em 0.5em; background-color: "+fillColorArray[i-1]+";'>"+
                                "</i><span style='margin: 0em 0.3em;'>"+(diffDivider*i)+"&nbsp;&ndash;&nbsp;"+(diffDivider*(i-1))+"</span></li>");
                        }

                        div.innerHTML = "<h3 style='text-align: center;'>"+evalGeoJson().legend+"</h3><ul style='list-style: none;'>"+label.join('')+"</ul>"
                        return div;
                    };
                    legend.addTo(myMap);

                    geoJson = L.geoJson(populateStateData(dataJson), {style: style, onEachFeature: onEachFeature}).addTo(myMap);
                }

            };

            // Get GeoJson Object
            var evalGeoJson = function(){
                return scope.$eval(attrs.geoJson);
            };

            // Watch for change in data
            scope.$watch(function(){return evalGeoJson().data;}, function(newVal){
                loadData(newVal);
            });

            // initialize map
            myMap = L.map(element[0],{attributionControl: false}).setView([37.0902, -95.7129], 4);
            L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png?{level}', {level: 'base'}).addTo(myMap);
        }
    };

});