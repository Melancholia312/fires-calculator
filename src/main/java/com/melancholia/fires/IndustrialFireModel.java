package com.melancholia.fires;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class IndustrialFireModel {

    // Входные параметры (X1-X15)
    @NotNull(message = "Пожароопасность технологического процесса обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x1; // Пожароопасность технологического процесса

    @NotNull(message = "Наличие горючих материалов обязательно")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x2; // Наличие горючих материалов

    @NotNull(message = "Эффективность системы вентиляции обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x3; // Эффективность системы вентиляции

    @NotNull(message = "Качество электрооборудования обязательно")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x4; // Качество электрооборудования

    @NotNull(message = "Температура в помещении обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x5; // Температура в помещении

    @NotNull(message = "Влажность воздуха обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x6; // Влажность воздуха

    @NotNull(message = "Концентрация кислорода обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x7; // Концентрация кислорода

    @NotNull(message = "Скорость воздушных потоков обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x8; // Скорость воздушных потоков

    @NotNull(message = "Уровень подготовки персонала обязателен")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x9; // Уровень подготовки персонала

    @NotNull(message = "Наличие автоматических систем пожаротушения обязательно")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x10; // Наличие автоматических систем пожаротушения

    @NotNull(message = "Состояние пожарных гидрантов обязательно")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x11; // Состояние пожарных гидрантов

    @NotNull(message = "Доступность эвакуационных путей обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x12; // Доступность эвакуационных путей

    @NotNull(message = "Качество пожарной сигнализации обязательно")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x13; // Качество пожарной сигнализации

    @NotNull(message = "Интенсивность внешних возмущений обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x14; // Интенсивность внешних возмущений

    @NotNull(message = "Площадь помещения обязательна")
    @Min(value = 0, message = "Значение должно быть неотрицательным")
    private Double x15; // Площадь помещения

    // Внешние воздействия
    private Double e1; // Техногенные факторы
    private Double e2; // Человеческий фактор
    private Double e3; // Природные факторы
    private Double e4; // Экономические факторы
    private Double e5; // Организационные факторы

    // Время моделирования
    private Integer simulationTime; // в минутах

    // Геттеры и сеттеры
    public Double getX1() { return x1; }
    public void setX1(Double x1) { this.x1 = x1; }

    public Double getX2() { return x2; }
    public void setX2(Double x2) { this.x2 = x2; }

    public Double getX3() { return x3; }
    public void setX3(Double x3) { this.x3 = x3; }

    public Double getX4() { return x4; }
    public void setX4(Double x4) { this.x4 = x4; }

    public Double getX5() { return x5; }
    public void setX5(Double x5) { this.x5 = x5; }

    public Double getX6() { return x6; }
    public void setX6(Double x6) { this.x6 = x6; }

    public Double getX7() { return x7; }
    public void setX7(Double x7) { this.x7 = x7; }

    public Double getX8() { return x8; }
    public void setX8(Double x8) { this.x8 = x8; }

    public Double getX9() { return x9; }
    public void setX9(Double x9) { this.x9 = x9; }

    public Double getX10() { return x10; }
    public void setX10(Double x10) { this.x10 = x10; }

    public Double getX11() { return x11; }
    public void setX11(Double x11) { this.x11 = x11; }

    public Double getX12() { return x12; }
    public void setX12(Double x12) { this.x12 = x12; }

    public Double getX13() { return x13; }
    public void setX13(Double x13) { this.x13 = x13; }

    public Double getX14() { return x14; }
    public void setX14(Double x14) { this.x14 = x14; }

    public Double getX15() { return x15; }
    public void setX15(Double x15) { this.x15 = x15; }

    public Double getE1() { return e1; }
    public void setE1(Double e1) { this.e1 = e1; }

    public Double getE2() { return e2; }
    public void setE2(Double e2) { this.e2 = e2; }

    public Double getE3() { return e3; }
    public void setE3(Double e3) { this.e3 = e3; }

    public Double getE4() { return e4; }
    public void setE4(Double e4) { this.e4 = e4; }

    public Double getE5() { return e5; }
    public void setE5(Double e5) { this.e5 = e5; }

    public Integer getSimulationTime() { return simulationTime; }
    public void setSimulationTime(Integer simulationTime) { this.simulationTime = simulationTime; }
}
