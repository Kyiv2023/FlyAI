// This file was generated by generate-classes.
// DO NOT EDIT THIS FILE!
#pragma once

#include "CesiumGltf/Accessor.h"
#include "CesiumGltf/Animation.h"
#include "CesiumGltf/Asset.h"
#include "CesiumGltf/Buffer.h"
#include "CesiumGltf/BufferView.h"
#include "CesiumGltf/Camera.h"
#include "CesiumGltf/Image.h"
#include "CesiumGltf/Library.h"
#include "CesiumGltf/Material.h"
#include "CesiumGltf/Mesh.h"
#include "CesiumGltf/Node.h"
#include "CesiumGltf/Sampler.h"
#include "CesiumGltf/Scene.h"
#include "CesiumGltf/Skin.h"
#include "CesiumGltf/Texture.h"

#include <CesiumUtility/ExtensibleObject.h>

#include <cstdint>
#include <string>
#include <vector>

namespace CesiumGltf {
/**
 * @brief The root object for a glTF asset.
 */
struct CESIUMGLTF_API ModelSpec : public CesiumUtility::ExtensibleObject {
  static inline constexpr const char* TypeName = "Model";

  /**
   * @brief Names of glTF extensions used in this asset.
   */
  std::vector<std::string> extensionsUsed;

  /**
   * @brief Names of glTF extensions required to properly load this asset.
   */
  std::vector<std::string> extensionsRequired;

  /**
   * @brief An array of accessors.
   *
   * An accessor is a typed view into a bufferView.
   */
  std::vector<CesiumGltf::Accessor> accessors;

  /**
   * @brief An array of keyframe animations.
   */
  std::vector<CesiumGltf::Animation> animations;

  /**
   * @brief Metadata about the glTF asset.
   */
  CesiumGltf::Asset asset;

  /**
   * @brief An array of buffers.
   *
   * A buffer points to binary geometry, animation, or skins.
   */
  std::vector<CesiumGltf::Buffer> buffers;

  /**
   * @brief An array of bufferViews.
   *
   * A bufferView is a view into a buffer generally representing a subset of the
   * buffer.
   */
  std::vector<CesiumGltf::BufferView> bufferViews;

  /**
   * @brief An array of cameras.
   *
   * A camera defines a projection matrix.
   */
  std::vector<CesiumGltf::Camera> cameras;

  /**
   * @brief An array of images.
   *
   * An image defines data used to create a texture.
   */
  std::vector<CesiumGltf::Image> images;

  /**
   * @brief An array of materials.
   *
   * A material defines the appearance of a primitive.
   */
  std::vector<CesiumGltf::Material> materials;

  /**
   * @brief An array of meshes.
   *
   * A mesh is a set of primitives to be rendered.
   */
  std::vector<CesiumGltf::Mesh> meshes;

  /**
   * @brief An array of nodes.
   */
  std::vector<CesiumGltf::Node> nodes;

  /**
   * @brief An array of samplers.
   *
   * A sampler contains properties for texture filtering and wrapping modes.
   */
  std::vector<CesiumGltf::Sampler> samplers;

  /**
   * @brief The index of the default scene.
   *
   * This property **MUST NOT** be defined, when `scenes` is undefined.
   */
  int32_t scene = -1;

  /**
   * @brief An array of scenes.
   */
  std::vector<CesiumGltf::Scene> scenes;

  /**
   * @brief An array of skins.
   *
   * A skin is defined by joints and matrices.
   */
  std::vector<CesiumGltf::Skin> skins;

  /**
   * @brief An array of textures.
   */
  std::vector<CesiumGltf::Texture> textures;

private:
  /**
   * @brief This class is not meant to be instantiated directly. Use {@link Model} instead.
   */
  ModelSpec() = default;
  friend struct Model;
};
} // namespace CesiumGltf
