package co.com.wen.api.util;

public class ValidationUtil {

	public static boolean isNumeric(String valor){
		try {
			Integer.parseInt(valor);
			return Boolean.TRUE;
		} catch (NumberFormatException e) {
			return Boolean.FALSE;
		}
	}

	public static boolean isNullOrEmpty(String valor){
		if (valor == null || valor.isEmpty()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public static int convertStringToInt(String valor){
		return Integer.parseInt(valor);
	}
}
