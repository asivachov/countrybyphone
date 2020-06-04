package lv.neotech.homework.ws.dto;

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
