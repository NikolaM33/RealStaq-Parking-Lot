#Parking Lot API

This is a RESTful API that allows you to find nearby parking lots and calculate location parking scores (calculates the parking score as the ratio of parking lots
   within 1km radius to the total number of parking lots).
   
Requirements:

    Java 8
    Elasticsearch 7.0
    Maven 
    
Running the API

 Clone the repository.
 Run elasticsearch 7.0.0 and set cluster name "real-staq" and cluster nodes(in application.yml file or elasticsearch.yml)
 Navigate to the root directory.
 Run Project from iIntelliJ (You need to use Java 8 SDK)

The API should be accessible at http://localhost:8080.

Running Loader 
  loader will run automacticlly after run application
  
 Api Endpoints:
  GET /parking-lot/nearest 
    Returns the closest parking lot to the specified location
  GET /parking-lot/calculate-location-score 
    Returns location parking score. (Ratio of parking lots within 1km radius to the total number of parking lots)

Usage:
  Postman collection (Parking-lot.postman_collection.json)
  curl calls (replace values with your values):
    -curl -X GET -H "Content-Type: application/json" http://localhost:8080/parking-lot/nearest -d "{\"latitude\": 40.7128, \"longitude\": -74.0060}"
    -curl -X GET -H "Content-Type: application/json" http://localhost:8080/parking-lot/calculate-location-score -d "{\"latitude\": 40.7128, \"longitude\": -74.0060}"
 
    
