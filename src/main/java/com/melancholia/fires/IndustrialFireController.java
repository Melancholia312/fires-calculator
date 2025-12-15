package com.melancholia.fires;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndustrialFireController {

    private final SystemDynamicsService dynamicsService;
    private final ObjectMapper objectMapper;

    public IndustrialFireController(SystemDynamicsService dynamicsService) {
        this.dynamicsService = dynamicsService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/")
    public String showCalculator(Model model) {
        IndustrialFireModel fireModel = new IndustrialFireModel();
        setDefaultValues(fireModel);
        model.addAttribute("fireModel", fireModel);
        return "calculator";
    }

    @PostMapping("/simulate")
    public String simulateFire(@Valid IndustrialFireModel fireModel,
                               BindingResult result,
                               Model model) throws JsonProcessingException {
        if (result.hasErrors()) {
            return "calculator";
        }

        SimulationResult simulationResult = dynamicsService.simulate(fireModel);
        model.addAttribute("result", simulationResult);
        model.addAttribute("fireModel", fireModel);

        // Преобразуем данные в JSON для JavaScript
        if (simulationResult != null) {
            model.addAttribute("timePointsJson", objectMapper.writeValueAsString(simulationResult.getTimePoints()));
            model.addAttribute("fireRiskLevelsJson", objectMapper.writeValueAsString(simulationResult.getFireRiskLevels()));
            model.addAttribute("temperatureValuesJson", objectMapper.writeValueAsString(simulationResult.getTemperatureValues()));
            model.addAttribute("oxygenConcentrationJson", objectMapper.writeValueAsString(simulationResult.getOxygenConcentration()));
        }

        return "calculator";
    }

    // IndustrialFireController.java - метод setDefaultValues
    private void setDefaultValues(IndustrialFireModel model) {
        // Устанавливаем значения, которые будут влиять на результат
        model.setX1(6.0);  // Средняя пожароопасность
        model.setX2(70.0); // Много горючих материалов
        model.setX3(60.0); // Средняя вентиляция
        model.setX4(8.0);  // Хорошее электрооборудование
        model.setX5(28.0); // Немного повышенная температура
        model.setX6(45.0); // Средняя влажность
        model.setX7(21.0); // Нормальный кислород
        model.setX8(2.0);  // Средняя скорость воздуха
        model.setX9(7.0);  // Хорошая подготовка персонала
        model.setX10(85.0); // Хорошие автоматические системы
        model.setX11(90.0); // Хорошие гидранты
        model.setX12(9.0);  // Хорошие пути эвакуации
        model.setX13(8.0);  // Хорошая сигнализация
        model.setX14(4.0);  // Средние внешние возмущения
        model.setX15(1500.0); // Средняя площадь

        model.setE1(6.0);  // Техногенные факторы
        model.setE2(5.0);  // Человеческий фактор
        model.setE3(3.0);  // Природные факторы
        model.setE4(4.0);  // Экономические факторы
        model.setE5(5.0);  // Организационные факторы

        model.setSimulationTime(60);
    }
}