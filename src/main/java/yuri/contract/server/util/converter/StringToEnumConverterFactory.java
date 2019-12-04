package yuri.contract.server.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import yuri.contract.server.model.util.EnumValue;

public final class StringToEnumConverterFactory implements ConverterFactory<String, EnumValue> {
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends EnumValue> Converter<String, T> getConverter(Class<T> aClass) {
        return new StringToEnum(aClass);
    }

    private static class StringToEnum<T extends Enum<T> & EnumValue> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String s) {
            s = s.trim();
            return s.isEmpty() ? null : EnumValue.valueOf(enumType, s);
        }
    }
}
