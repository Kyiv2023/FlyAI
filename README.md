# FlyAI
Платформа машинного навчання для розвитку дронової інтелектуальності: симулятор, додаток для управління на Android (сумісний з DJI Mavic), інструменти для збору та обробки наборів даних, фреймворк та веб-сайт для навчання моделей



## Структура

### [LikeAFly](https://github.com/Kyiv2023/FlyAI/tree/main/LikeAFly)

Додаток для керування дроном на базі Android із підтримкою машинного навчання

 - Керування дроном DJI Mavic
 - Моделювання керування дроном **AirSim/Colosseum**
 - [Esri GIS](https://www.esri.com/) тривимірна/плоска карта
 - Телеметрія/керування/логування зображень у хмару

### [FlyDataTools](https://github.com/Kyiv2023/FlyAI/tree/main/FlyDataTools)

Набір інструментів для навчання моделі, здатної виводити висновки за телеметрією і зображеннями дрона:

 - Сегментація транспортних засобів на зображеннях
 - Прогнозування координат GPS спостережуваних транспортних засобів
 - Підхід зі створенням послідовності керування
 - Прогнозування координат GPS дрона


### [CesiumWorld](https://github.com/Kyiv2023/FlyAI/tree/main/CesiumWorld)

Середовище для моделювання світу: додаток Unreal з:
  - Плагін [Cesium](https://cesium.com/), що забезпечує реалістичне відображення реальної карті світу
  - [AirSim](https://microsoft.github.io/AirSim/) (повинен бути замінений на [Colosseum](https://github.com/CodexLabsLLC/Colosseum)), який надає API/інструменти для моделювання дрона
  - Моделі транспортних засобів від [Vigilante](https://www.unrealengine.com/marketplace/en-US/profile/Vigilante)

### [OBSfucatorium](https://github.com/Kyiv2023/FlyAI/tree/main/OBSfucatorium)

https://github.com/Kyiv2023/FlyAI/assets/135607224/cada7d87-b65e-413a-b799-9592c829c3b5

https://github.com/Kyiv2023/FlyAI/assets/135607224/656fa92b-ad95-47fe-bfff-30eef73c70df

Моделюється в Unreal з використанням Airsim MS гілки Colosseum, яка працює в контейнері Docker на Linux-машині і показує сцен



## Structure

### [LikeAFly](https://github.com/Kyiv2023/FlyAI/tree/main/LikeAFly)

Android ML powered drone control application

 - DJI Mavic control
 - Simulated **AirSim/Colosseum** drone control
 - [Esri GIS](https://www.esri.com/) 3d/flat map
 - telemetry/controls/image logging to cloud

### [FlyDataTools](https://github.com/Kyiv2023/FlyAI/tree/main/FlyDataTools)

Toolset for training a model that is capable of inferring by drone telemetry/drone imagery:

 - Vehicle on imagery segmentation
 - Observered vehicle GPS coordinate prediction
 - Control sequence approach
 - Drone GPS coordinate prediction


### [CesiumWorld](https://github.com/Kyiv2023/FlyAI/tree/main/CesiumWorld)

An enviroment for simulating world: Unreal (alternatively Unity) application with:
  - [Cesium](https://cesium.com/) plugin provides realistic representation of the real world map
  - [AirSim](https://microsoft.github.io/AirSim/) (should be replaced with [Colosseum](https://github.com/CodexLabsLLC/Colosseum)) provides API/tools for drone simulation
  - [Vigilante](https://www.unrealengine.com/marketplace/en-US/profile/Vigilante) provides models of vehicles

### [OBSfucatorium](https://github.com/Kyiv2023/FlyAI/tree/main/OBSfucatorium)



https://github.com/Kyiv2023/FlyAI/assets/135607224/cada7d87-b65e-413a-b799-9592c829c3b5



https://github.com/Kyiv2023/FlyAI/assets/135607224/656fa92b-ad95-47fe-bfff-30eef73c70df



Simulated in Unity/Unreal with Airsim MS fork Colosseum running in docker container on linux machine demonstrates a scene of the summer/winter/spring environment that includes people/vehicles of the two countries. per python script domain specific action (like spawn.vehicle(gps coordinate)) it spawns an object on given point, and drone, piloted from the Android application, after traveling to the object - via Python script - takes the photos of the object - in the Android application running in simulator. then it gets uploaded to the Google Cloud Storage, where it's being processed - a labels recorded in Airsim MS fork Colosseum/photos taken by Android app are machine learned (Colloseum per api call switches the image mode: regular Unity/Unreal rendering or thermal/depth/segmented vision, so Android app takes 3 photos before it sends "drop" command to Colloseum) are Machine Learned against the Colloseum-logged positions.
