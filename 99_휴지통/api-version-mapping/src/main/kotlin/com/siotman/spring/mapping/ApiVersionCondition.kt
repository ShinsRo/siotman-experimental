package com.siotman.spring.mapping

import net.swiftzer.semver.SemVer
import org.springframework.web.servlet.mvc.condition.RequestCondition
import javax.servlet.http.HttpServletRequest

const val API_VERSION_HEADER = "Shins-Api-Version"

class ApiVersionCondition(
    private val ann: ApiVersion,
    private val headerName: String = API_VERSION_HEADER
) : RequestCondition<ApiVersionCondition> {

    val from: SemVer
    val to: SemVer

    init {
        from = SemVer.parse(ann.from)
        to = SemVer.parse(ann.to)

        require(from <= to) { "버전 형식이 올바르지 않습니다.(from-to : $from-$to)" }
    }

    override fun combine(other: ApiVersionCondition): ApiVersionCondition {
        throw RuntimeException("ApiVersion 은 클래스와 메소드에 횬용하여 사용할 수 없습니다.")
    }

    override fun getMatchingCondition(request: HttpServletRequest): ApiVersionCondition? {
        val versionStr = request.getHeader(headerName)
        if (versionStr.isNullOrBlank()) return null

        val version = try {
            SemVer.parse(versionStr)
        } catch (e: IllegalArgumentException) {
            null
        } ?: return null

        return if (version in from..to) this
        else null
    }

    override fun compareTo(other: ApiVersionCondition, request: HttpServletRequest): Int {
        return if (this == other) 0
        else throw RuntimeException("ApiVersion 은 클래스와 메소드에 횬용하여 사용할 수 없습니다.")
    }

    /**
     * *ApiVersionCondition* 의 hashCode() 의 리턴은 항상 동일하다. 한편, equals() 는 other 의 버전 범위에 대해 교집합이 존재하는 경우, true 를 반환한다.
     *
     * Spring 에서 어플리케이션을 띄우며, Condition 들을 조합한 RequestMappingInfo 의 핸들러 메소드는 HashMap<RequestMappingInfo, Method> 레지스트리에 등록한다.
     * 그리고 Spring RequestMappingInfo 에 대한 핸들러 Method 의 unique 검증은 등록 시 이루어진다. 해쉬 값을 동일하게 한 이유는 이 시점에서 예외를 겪게 하기 위함이다.
     *
     * HashMap 에서 get 할 때, key 는 hashCode 와 equals 를 통해 비교되는 한편, ( @see **[java.util.HashMap.getNode]** )
     * ApiVersion 은 복수의 프로퍼티 [from, to] 로 정의하는 "범위" 에 대해 유일해야하므로, 범위 교집합을 피하는 hashCode 를 정의할 수 없다.
     *
     * 따라서, hashCode 는 동일하게 리턴하고, equals 는 교집합이 존재하는 경우 true 를 반환하게 함으로써,
     * 핸들러 Method 유일함을 검증할 때  ( @see **[org.springframework.web.servlet.handler.AbstractHandlerMethodMapping]** 의 validateMethodMapping 함수 ),
     * HashMap<RequestMappingInfo, Method> 상 key 가 이미 존재함을 알 수 있도록 하여, 중복이 있다면 오류가 발생할 수 있게끔한다.
     */
    override fun equals(other: Any?): Boolean {
        if (other !is ApiVersionCondition) return false

        val range = from..to

        // 교집합 버전 정의가 존재하는 경우, 동일한 조건으로 간주하여 예외를 유도한다.
        return other.from in range || other.to in range
    }

    override fun hashCode(): Int {
        return ApiVersion::class.simpleName.hashCode()
    }
}