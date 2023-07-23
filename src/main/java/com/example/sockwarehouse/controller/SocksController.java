package com.example.sockwarehouse.controller;

import com.example.sockwarehouse.model.Color;
import com.example.sockwarehouse.model.Size;
import com.example.sockwarehouse.model.SocksBatch;
import com.example.sockwarehouse.model.operation.SocksOperation;
import com.example.sockwarehouse.service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/socks")
@Tag(name = "Носки", description = "Операции для работы с носками")
@ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")
public class SocksController {

    private final SocksService socksService;

    @PostMapping
    @Operation(summary = "Регистрация прихода товара на склад")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалось добавить приход",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SocksOperation.class)))),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат")
    })
    public ResponseEntity<SocksOperation> accept(@RequestBody SocksBatch socksBatch) {
        SocksOperation socksOperation = socksService.accept(socksBatch);
        return ResponseEntity.ok(socksOperation);
    }

    @PutMapping
    @Operation(summary = "Регистрация отпуска товара со склада")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалось произвести отпуск носков со склада",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SocksOperation.class)))),
            @ApiResponse(responseCode = "400", description = "Товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат")
    })
    public ResponseEntity<SocksOperation> issuance(@RequestBody SocksBatch socksBatch) {
        SocksOperation socksOperation = socksService.issuance(socksBatch);
        if (socksOperation == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(socksOperation);
    }

    @GetMapping
    @Operation(summary = "Возврат общего количества носков на складе, соответствующих переданным в параметрах критериям запроса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен, результат в теле ответа в виде строкового представления целого числа",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат")
    })
    public ResponseEntity<Long> getQuantity(@RequestParam Color color,
                                                 @RequestParam Size size,
                                                 @RequestParam int cottonMin,
                                                 @RequestParam int cottonMax) {
        long socksQuantity = socksService.getQuantity(color, size, cottonMin, cottonMax);
        return ResponseEntity.ok(socksQuantity);
    }

    @DeleteMapping
    @Operation(summary = "Регистрация списания испорченных (бракованных) носков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос выполнен, товар списан со склада",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SocksOperation.class)))),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат")
    })
    public ResponseEntity<SocksOperation> reject(@RequestBody SocksBatch socksBatch) {
        SocksOperation socksOperation = socksService.reject(socksBatch);
        if (socksOperation == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(socksOperation);
    }
}
