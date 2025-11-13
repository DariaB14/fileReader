package com.example.filereader.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    public int findNMinNumber(String path, int n) throws IOException{
        validateParameters(path, n);

        int[] numbers=readFile(path);

        if(numbers.length == 0){
            throw new IllegalArgumentException("File doesn`t contain integers");
        }

        if(n>numbers.length){
            throw new IllegalArgumentException("N cannot be greater than the number of elements");
        }

        return findNMinInFile(numbers, n);
    }

    private void validateParameters(String path, int n){
        if(n<=0){
            throw new IllegalArgumentException("N should be positive");
        }
        if(path==null || path.trim().isEmpty()){
            throw new IllegalArgumentException("File path cannot be empty");
        }

        if(!path.toLowerCase().endsWith(".xlsx")){
            throw new IllegalArgumentException("This format is not supported");
        }

        File file = new File(path);
        if(!file.exists()){
            throw new IllegalArgumentException("File doesn`t exist");
        }
    }

    private int[] readFile(String path) throws IOException{
        try(FileInputStream file = new FileInputStream(path); Workbook workbook = new XSSFWorkbook(file)){
            Sheet sheet = workbook.getSheetAt(0);
            List<Integer> numbers = new ArrayList<>();

            for (Row row : sheet) {
                for (Cell cell : row) {
                    numbers.add((int) cell.getNumericCellValue());
                }
            }

            return numbers.stream().mapToInt(i -> i).toArray();
        }
    }

    private int findNMinInFile(int[] numbers, int n){
        int[] heap = new int[n];
        int heapSize = 0;

        for(int num : numbers){
            if(heapSize<n){
                heap[heapSize]=num;
                heapSize++;
                siftUp(heap, heapSize-1);
            } else if (num < heap[0]) {
                heap[0] = num;
                siftDown(heap, 0, heapSize);
            }
        }
        return heap[0];
    }

    private void siftUp(int[] heap, int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap[index] <= heap[parentIndex]) {
                break;
            }
            swap(heap, index, parentIndex);
            index = parentIndex;
        }
    }

    private void siftDown(int[] heap, int index, int heapSize) {
        while (index < heapSize) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int large = index;

            if (left < heapSize && heap[left] > heap[large]) {
                large = left;
            }
            if (right < heapSize && heap[right] > heap[large]) {
                large = right;
            }

            if (large == index) {
                break;
            }
            swap(heap, index, large);
            index = large;
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
