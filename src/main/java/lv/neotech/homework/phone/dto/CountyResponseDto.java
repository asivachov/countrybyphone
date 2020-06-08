package lv.neotech.homework.phone.dto;

public class CountyResponseDto {
    private final String country;

    public CountyResponseDto(String country) {
        this.country = country;
    }

    @SuppressWarnings("unused")
    public String getCountry() {
        return country;
    }

}
