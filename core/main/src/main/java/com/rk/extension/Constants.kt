package com.rk.extension

// PocketCode's own extension registry, hosted as static JSON + assets on Cloudflare R2
// behind resources.bfoxnet.com (bucket pocketcode-resoures). See the bucket's README.md
// for the layout and how to publish new packages. R2 is static hosting, so
// REVIEWS_API_BASE points at a stub file returning an empty ReviewsResponse.
const val EXTENSION_API_BASE = "https://resources.bfoxnet.com/extensions"
const val THEMES_API_BASE = "https://resources.bfoxnet.com/themes"
const val ICONPACKS_API_BASE = "https://resources.bfoxnet.com/icon-packs"
const val REVIEWS_API_BASE = "https://resources.bfoxnet.com/reviews.json"
