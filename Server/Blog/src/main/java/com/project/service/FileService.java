package com.project.service;

import com.project.dao.FileDao;
import com.project.dao.TagDao;
import com.project.dto.FileDto;

import javax.sql.DataSource;
import java.util.List;

public class FileService {
    private FileDao fileDao;
    public FileService(DataSource dataSource){
        this.fileDao = new FileDao(dataSource);
    }

    public List<FileDto> getAllFile() { return fileDao.selectAll(); }
    public List<FileDto> getFile(int file_id){ return fileDao.select(file_id); }
    public void addFile(FileDto fileDto) { fileDao.insert(fileDto); }
    public void updateFile(FileDto fileDto) { fileDao.update(fileDto); }
    public void deleteFile(int file_id) { fileDao.delete(file_id); }
}