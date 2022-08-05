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
    public List<FileDto> getFileByBoardID(int board_id) { return fileDao.selectByBoardID(board_id); }
    public void addFile(FileDto fileDto) { fileDao.insert(fileDto); }
    public void updateBoardId(int boardID, String fileName) { fileDao.updateBoardID(boardID, fileName); }
    public void deleteFile(int file_id) { fileDao.delete(file_id); }
}
