package com.joolove.core.domain.base_item;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAlcoholFactory {

    private static final Map<String, Class<? extends BaseAlcoholImpl>> registeredTypes = new HashMap<>();

    static {
        registeredTypes.put("일반 증류주", Spirits.class);
        registeredTypes.put("리큐르", Liqueur.class);
        registeredTypes.put("위스키", Whiskey.class);
        registeredTypes.put("브랜디", Brandy.class);
        registeredTypes.put("청주", Cheongju.class);
        registeredTypes.put("국산 맥주", DomesticBeer.class);
        registeredTypes.put("중국술", ChineseLiquor.class);
        registeredTypes.put("사케", Sake.class);
        registeredTypes.put("와인", Wine.class);
        registeredTypes.put("과실주", FruitWine.class);
        registeredTypes.put("수입 맥주", ImportedBeer.class);
        registeredTypes.put("일반 소주", Soju.class);
        registeredTypes.put("탁주", Takju.class);
        registeredTypes.put("전통 소주", TraditionalSoju.class);
        registeredTypes.put("기타 주류", BaseAlcoholImpl.class);
    }

    public static void registerType(String type, Class<? extends BaseAlcoholImpl> cls) {
        registeredTypes.put(type, cls);
    }

    public static BaseAlcoholImpl create(Object... args) {
        BaseAlcoholImpl pickedAlcohol = null;

        try {
            pickedAlcohol = getBaseAlcohol(args);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return pickedAlcohol;
    }

    // Dynamic factory method
    private static BaseAlcoholImpl getBaseAlcohol(Object... args)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        for (Object arg : args) {
            if (arg.)
        }

        // 1. 원하는 Class 객체를 가져옴
        Class<?> cls = registeredTypes.get();

        // 2. 생성자를 가져옴
        Constructor<?> alcoholConstructor = cls.getDeclaredConstructor(String.class, String.class, String.class);

        // 3. Reflection API 를 통해 인스턴스화를 하고 업캐스팅 하여 반환
        return (BaseAlcoholImpl) alcoholConstructor.newInstance(name, engName, type);
    }
}
