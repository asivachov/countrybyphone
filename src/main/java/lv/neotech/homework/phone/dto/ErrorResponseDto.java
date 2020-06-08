package lv.neotech.homework.phone.dto;

public class ErrorResponseDto {
    private final int code;
    private final String message;

    public ErrorResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @SuppressWarnings("unused")
    public int getCode() {
        return code;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

}
