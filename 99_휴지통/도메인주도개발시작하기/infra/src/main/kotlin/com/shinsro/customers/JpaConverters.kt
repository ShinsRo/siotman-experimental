package com.shinsro.customers

import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * 뭔가 좋은 방법이 없을까...?
 * 컨버터 이거 일일이 정의하는 건 좀 아닌 것 같은데...
 * 이런식이면 Jackson, Mongo, JPA 등등... 이런거 다 커버해야겠네?
 *
 * 개별 타입의 Value Object 가 좋은 방법이긴한데... 좋은 practice 없을까...
 *
 * 부모 클래스인 StringValue 의 컨버터를 만들면,
 * 자식 클래스를 컨버팅 시, convertToEntityAttribute 의 리턴타입이 StringValue 이므로, 자식 클래스를 리턴할 수 없다.
 *
 * StringValue 리턴을 허용하면, 최종적으로, setter 에서 부모클래스 오브젝트를 자식클래스타입 변수에 할당하는 꼴이 되어버려 오류가 발생한다.
 */

@Converter(autoApply = true)
class CustomerIdJpaConverter : AttributeConverter<CustomerId?, String?> {
    override fun convertToDatabaseColumn(attribute: CustomerId?): String? = attribute?.toString()
    override fun convertToEntityAttribute(dbData: String?): CustomerId? = dbData?.let(::CustomerId)
}

@Converter(autoApply = true)
class CustomerNameJpaConverter : AttributeConverter<CustomerName?, String?> {
    override fun convertToDatabaseColumn(attribute: CustomerName?): String? = attribute?.toString()
    override fun convertToEntityAttribute(dbData: String?): CustomerName? = dbData?.let(::CustomerName)
}
