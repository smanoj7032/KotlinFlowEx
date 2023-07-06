package com.example.koltinflowex.data.model

data class CommentModel(
    val postId: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val body: String? = null
)

data class AllPosts(
    val code: Int,
    val goodsDeliveryMessage: String,
    val recordsFiltered: Int,
    val recordsTotal: Int,
    val result: List<Result>,
    val status: String
)

data class Result2(
    val adminPost: String,
    val adsEndsDate: String,
    val adsStartDate: String,
    val advertisementStatus: String,
    val applied: String,
    val blockStatus: String,
    val consigneeFirstName: String,
    val consigneeId: Int,
    val consigneeLastName: String,
    val consigneeUserName: String,
    val dateOfLoading: String,
    val deleted: String,
    val destLat: String,
    val destLong: String,
    val destinationAddress: String,
    val destinationCountry: String,
    val destinationDepartmento: String,
    val destinationDistrict: String,
    val destinationProvince: String,
    val driverId: Int,
    val driverUserName: String,
    val expectedDeliveryDate: String,
    val freightAmount: Double,
    val goods: String,
    val goodsValue: Double,
    val goodsVolume: String,
    val isPagePost: String,
    val isUserFriend: String,
    val liked: Boolean,
    val likes: Int,
    val loadCarryingCapacity: String,
    val longTermPostPeriodDto: LongTermPostPeriodDto,
    val longTermPostRootDto: List<Any>,
    val noOfComments: Int,
    val pageName: String,
    val pageUrl: String,
    val postCreatedDate: String,
    val postId: Int,
    val postMedia: List<PostMedia>,
    val postText: String,
    val postType: String,
    val remarks: String,
    val rewardAmnt: Double,
    val rewarded: Boolean,
    val socialPostVisibility: String,
    val sourceAddress: String,
    val sourceCountry: String,
    val sourceDepartmento: String,
    val sourceDistrict: String,
    val sourceLat: String,
    val sourceLong: String,
    val sourceProvince: String,
    val status: String,
    val subPost: String,
    val taggedUsersList: List<Any>,
    val type: String,
    val unLiked: Boolean,
    val unlikes: Int,
    val user: User,
    val userId: Int
)

class LongTermPostPeriodDto

data class PostMedia(
    val mediaType: String,
    val mediaurl: String
)

data class User(
    val aboutCompany: String,
    val approvedStatus: String,
    val areaAvailability: Double,
    val blockStatus: String,
    val chargerPerKm: Double,
    val cityResidence: String,
    val companyAccountNumber: String,
    val companyName: String,
    val companyTaxNumber: String,
    val country: String,
    val countryCitizenship: String,
    val countryCode: String,
    val countryResidence: String,
    val currency: String,
    val deleted: String,
    val driverAddress: String,
    val driverLicenseBackImageUrl: String,
    val driverLicenseFrontImageUrl: String,
    val driverLicenseNumber: String,
    val education: String,
    val email: String,
    val firstName: String,
    val hobbies: String,
    val insuranceNumber: String,
    val languages: String,
    val lastName: String,
    val latitude: String,
    val licenseBackImage: String,
    val licenseFrontImage: String,
    val licenseNumber: String,
    val longitude: String,
    val mobilePhone: String,
    val ocupation: String,
    val otpVerified: String,
    val password: String,
    val profileImageUrl: String,
    val religion: String,
    val roleName: String,
    val userId: Int,
    val userName: String,
    val walletAmount: Double
)
