# FlyAI
Платформа машинного навчання для розвитку дронової інтелектуальності: симулятор, додаток для управління на Android (сумісний з DJI Mavic), інструменти для збору та обробки наборів даних, фреймворк та веб-сайт для навчання моделей





https://github.com/Kyiv2023/FlyAI/assets/135607224/cada7d87-b65e-413a-b799-9592c829c3b5



https://github.com/Kyiv2023/FlyAI/assets/135607224/656fa92b-ad95-47fe-bfff-30eef73c70df



Simulated in Unity/Unreal with Airsim MS fork Colosseum running in docker container on linux machine demonstrates a scene of the summer/winter/spring environment that includes people/vehicles of the two countries. per python script domain specific action (like spawn.vehicle(gps coordinate)) it spawns an object on given point, and drone, piloted from the Android application, after traveling to the object - via Python script - takes the photos of the object - in the Android application running in simulator. then it gets uploaded to the Google Cloud Storage, where it's being processed - a labels recorded in Airsim MS fork Colosseum/photos taken by Android app are machine learned (Colloseum per api call switches the image mode: regular Unity/Unreal rendering or thermal/depth/segmented vision, so Android app takes 3 photos before it sends "drop" command to Colloseum) are Machine Learned against the Colloseum-logged positions.
