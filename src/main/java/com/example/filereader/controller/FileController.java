package com.example.filereader.controller;

import com.example.filereader.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Find N-th minimum element in Excel file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully found N-th minimum element"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping("/search")
    public ResponseEntity<?> findNMinNumber(@Parameter(description = "Path to.xlsx file)", example = "C:/documents/numbers.xlsx") @RequestParam String path,
                                            @Parameter(description = "N-th minimum number (from 1)", example = "3") @RequestParam int n){
        try{
            int result=fileService.findNMinNumber(path, n);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Internal error");
        }
    }
}
