package com.melancholia.fires;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemDynamicsService {

    // Весовые коэффициенты для каждой переменной
    private static final double[] WEIGHTS = {
            0.15,  // X1 - Пожароопасность технологического процесса
            0.12,  // X2 - Наличие горючих материалов
            -0.10, // X3 - Эффективность системы вентиляции (отрицательный - снижает риск)
            -0.08, // X4 - Качество электрооборудования
            0.10,  // X5 - Температура в помещении
            -0.05, // X6 - Влажность воздуха
            0.12,  // X7 - Концентрация кислорода
            0.07,  // X8 - Скорость воздушных потоков
            -0.06, // X9 - Уровень подготовки персонала
            -0.09, // X10 - Автоматические системы пожаротушения
            -0.07, // X11 - Состояние пожарных гидрантов
            -0.08, // X12 - Доступность эвакуационных путей
            -0.05, // X13 - Качество пожарной сигнализации
            0.14,  // X14 - Интенсивность внешних возмущений
            0.08   // X15 - Площадь помещения
    };

    public SimulationResult simulate(IndustrialFireModel model) {
        SimulationResult result = new SimulationResult();

        // Нормализация входных параметров
        double[] normalizedParams = normalizeInputParameters(model);

        // Внешние воздействия
        double[] externalFactors = getExternalFactors(model);

        // Параметры для моделирования
        int simulationTime = model.getSimulationTime() != null ? model.getSimulationTime() : 60;
        double dt = 1.0; // шаг в минутах

        // Начальные условия
        double fireRisk = calculateInitialRisk(normalizedParams, externalFactors);
        double temperature = model.getX5(); // начальная температура
        double oxygen = model.getX7(); // начальная концентрация кислорода
        double smoke = 0.0; // начальная концентрация дыма

        // Списки для хранения результатов
        List<Double> timePoints = new ArrayList<>();
        List<Double> fireRisks = new ArrayList<>();
        List<Double> temperatures = new ArrayList<>();
        List<Double> oxygenLevels = new ArrayList<>();
        List<Double> smokeLevels = new ArrayList<>();

        double maxRisk = 0;
        double criticalTime = -1;

        // Динамическое моделирование
        for (int t = 0; t < simulationTime; t++) {
            timePoints.add((double) t);

            // Расчет скорости изменения параметров
            double dRisk = calculateRiskChange(fireRisk, normalizedParams, externalFactors, temperature, oxygen);
            double dTemperature = calculateTemperatureChange(temperature, fireRisk, normalizedParams[0], normalizedParams[4]);
            double dOxygen = calculateOxygenChange(oxygen, fireRisk, normalizedParams[6]);
            double dSmoke = calculateSmokeChange(smoke, fireRisk);

            // Обновление параметров
            fireRisk = Math.max(0, Math.min(1, fireRisk + dRisk * dt));
            temperature = Math.max(20, Math.min(100, temperature + dTemperature * dt));
            oxygen = Math.max(5, Math.min(25, oxygen + dOxygen * dt));
            smoke = Math.max(0, Math.min(100, smoke + dSmoke * dt));

            // Сохранение результатов
            fireRisks.add(fireRisk);
            temperatures.add(temperature);
            oxygenLevels.add(oxygen);
            smokeLevels.add(smoke);

            // Обновление максимального риска
            if (fireRisk > maxRisk) {
                maxRisk = fireRisk;
            }

            // Определение критического времени
            if (fireRisk > 0.7 && criticalTime < 0) {
                criticalTime = t;
            }
        }

        // Заполнение результата
        result.setTimePoints(timePoints);
        result.setFireRiskLevels(fireRisks);
        result.setTemperatureValues(temperatures);
        result.setOxygenConcentration(oxygenLevels);
        result.setSmokeConcentration(smokeLevels);

        result.setMaxFireRisk(maxRisk);
        result.setTimeToCritical(criticalTime);
        result.setRiskLevel(determineRiskLevel(maxRisk));
        result.setRecommendations(generateRecommendations(normalizedParams, maxRisk, criticalTime));

        return result;
    }

    private double[] normalizeInputParameters(IndustrialFireModel model) {
        double[] normalized = new double[15];

        // X1: Пожароопасность технологического процесса (0-10 -> 0-1)
        normalized[0] = model.getX1() / 10.0;

        // X2: Наличие горючих материалов (0-100 кг/м² -> 0-1)
        normalized[1] = model.getX2() / 100.0;

        // X3: Эффективность системы вентиляции (0-100% -> 0-1, инвертируем: 1-0)
        normalized[2] = 1.0 - (model.getX3() / 100.0);

        // X4: Качество электрооборудования (0-10 -> 0-1, инвертируем: 1-0)
        normalized[3] = 1.0 - (model.getX4() / 10.0);

        // X5: Температура в помещении (20-100°C -> 0-1)
        normalized[4] = (model.getX5() - 20) / 80.0;

        // X6: Влажность воздуха (0-100% -> 0-1, инвертируем: 1-0)
        normalized[5] = 1.0 - (model.getX6() / 100.0);

        // X7: Концентрация кислорода (0-25% -> 0-1)
        normalized[6] = model.getX7() / 25.0;

        // X8: Скорость воздушных потоков (0-10 м/с -> 0-1)
        normalized[7] = model.getX8() / 10.0;

        // X9: Уровень подготовки персонала (0-10 -> 0-1, инвертируем: 1-0)
        normalized[8] = 1.0 - (model.getX9() / 10.0);

        // X10: Наличие автоматических систем (0-100% -> 0-1, инвертируем: 1-0)
        normalized[9] = 1.0 - (model.getX10() / 100.0);

        // X11: Состояние пожарных гидрантов (0-100% -> 0-1, инвертируем: 1-0)
        normalized[10] = 1.0 - (model.getX11() / 100.0);

        // X12: Доступность эвакуационных путей (0-10 -> 0-1, инвертируем: 1-0)
        normalized[11] = 1.0 - (model.getX12() / 10.0);

        // X13: Качество пожарной сигнализации (0-10 -> 0-1, инвертируем: 1-0)
        normalized[12] = 1.0 - (model.getX13() / 10.0);

        // X14: Интенсивность внешних возмущений (0-10 -> 0-1)
        normalized[13] = model.getX14() / 10.0;

        // X15: Площадь помещения (0-10000 м² -> 0-1)
        normalized[14] = Math.min(1.0, model.getX15() / 5000.0);

        return normalized;
    }

    private double[] getExternalFactors(IndustrialFireModel model) {
        double[] factors = new double[5];

        factors[0] = model.getE1() != null ? model.getE1() / 10.0 : 0.5; // Техногенные
        factors[1] = model.getE2() != null ? model.getE2() / 10.0 : 0.5; // Человеческий
        factors[2] = model.getE3() != null ? model.getE3() / 10.0 : 0.5; // Природные
        factors[3] = model.getE4() != null ? model.getE4() / 10.0 : 0.5; // Экономические
        factors[4] = model.getE5() != null ? model.getE5() / 10.0 : 0.5; // Организационные

        return factors;
    }

    private double calculateInitialRisk(double[] params, double[] externalFactors) {
        double baseRisk = 0.3; // Базовый риск

        // Учет параметров системы
        for (int i = 0; i < params.length; i++) {
            baseRisk += WEIGHTS[i] * params[i];
        }

        // Учет внешних факторов
        double externalInfluence = 0;
        for (double factor : externalFactors) {
            externalInfluence += factor;
        }
        baseRisk += 0.1 * (externalInfluence - 2.5); // Нормализация

        return Math.max(0, Math.min(1, baseRisk));
    }

    private double calculateRiskChange(double currentRisk, double[] params, double[] externalFactors,
                                       double temperature, double oxygen) {
        double change = 0;

        // Влияние температуры (чем выше температура, тем быстрее растет риск)
        change += 0.001 * (temperature - 25);

        // Влияние кислорода (больше кислорода - выше риск возгорания)
        change += 0.002 * (oxygen - 21);

        // Влияние внешних факторов
        double externalImpact = 0;
        for (double factor : externalFactors) {
            externalImpact += factor;
        }
        change += 0.0005 * externalImpact;

        // Нелинейность: при высоком риске он растет быстрее
        if (currentRisk > 0.5) {
            change *= (1 + 2 * (currentRisk - 0.5));
        }

        // Случайные флуктуации
        change += (Math.random() - 0.5) * 0.01;

        return change;
    }

    private double calculateTemperatureChange(double currentTemp, double fireRisk,
                                              double processHazard, double initialTemp) {
        double change = 0;

        // Нагрев от пожарного риска
        change += 0.1 * fireRisk;

        // Нагрев от технологического процесса
        change += 0.05 * processHazard;

        // Естественное охлаждение (стремится к начальной температуре)
        change -= 0.02 * (currentTemp - initialTemp * 80 - 20);

        // Нелинейный эффект при высоких температурах
        if (currentTemp > 60) {
            change += 0.02 * (currentTemp - 60);
        }

        return change;
    }

    private double calculateOxygenChange(double currentOxygen, double fireRisk, double initialOxygen) {
        double change = 0;

        // Потребление кислорода при горении
        change -= 0.05 * fireRisk;

        // Восстановление кислорода (стремится к начальному уровню)
        change += 0.01 * (initialOxygen * 25 - currentOxygen);

        // Резкое падение при сильном пожаре
        if (fireRisk > 0.8) {
            change -= 0.1;
        }

        return change;
    }

    private double calculateSmokeChange(double currentSmoke, double fireRisk) {
        double change = 0;

        // Образование дыма пропорционально риску
        change += 0.5 * fireRisk;

        // Рассеивание дыма
        change -= 0.05 * currentSmoke;

        return change;
    }

    private String determineRiskLevel(double maxRisk) {
        if (maxRisk >= 0.8) return "КРИТИЧЕСКИЙ";
        if (maxRisk >= 0.6) return "ВЫСОКИЙ";
        if (maxRisk >= 0.4) return "СРЕДНИЙ";
        if (maxRisk >= 0.2) return "НИЗКИЙ";
        return "МИНИМАЛЬНЫЙ";
    }

    private List<String> generateRecommendations(double[] params, double maxRisk, double criticalTime) {
        List<String> recommendations = new ArrayList<>();

        if (maxRisk > 0.6) {
            recommendations.add("⚠️ Требуется немедленное вмешательство!");
            recommendations.add("🚨 Подготовьте систему пожаротушения к работе");
            recommendations.add("📢 Оповестите персонал о повышенной опасности");
        }

        if (criticalTime > 0 && criticalTime < 30) {
            recommendations.add("⏰ Критическая ситуация может наступить через " + (int)criticalTime + " минут");
        }

        // Рекомендации по конкретным параметрам
        if (params[0] > 0.7) { // Высокая пожароопасность процесса
            recommendations.add("🔧 Усильте контроль за технологическим процессом");
        }

        if (params[1] > 0.5) { // Много горючих материалов
            recommendations.add("📦 Уменьшите количество горючих материалов на складе");
        }

        if (params[2] < 0.3) { // Плохая вентиляция (normalized[2] = 1 - эффективность)
            recommendations.add("💨 Проверьте и улучшите систему вентиляции");
        }

        if (params[3] < 0.4) { // Плохое электрооборудование
            recommendations.add("🔌 Проведите проверку электрооборудования");
        }

        if (params[4] > 0.6) { // Высокая температура
            recommendations.add("🌡️ Снизьте температуру в помещении");
        }

        if (params[9] < 0.3) { // Плохие автоматические системы
            recommendations.add("🚒 Установите или модернизируйте системы автоматического пожаротушения");
        }

        if (params[8] < 0.4) { // Низкая подготовка персонала
            recommendations.add("👨‍🏫 Проведите обучение персонала по пожарной безопасности");
        }

        return recommendations;
    }
}