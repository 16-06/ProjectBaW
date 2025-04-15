package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class VoteDto {

    public static class RequestDto {
        private String name;
        private String author;
        private String category;
        private byte[] imagedata;
        private List<VoteOptionDto.RequestDto> options;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public byte[] getImagedata() { return imagedata; }
        public void setImagedata(byte[] imagedata) { this.imagedata = imagedata; }

        public List<VoteOptionDto.RequestDto> getOptions() { return options; }
        public void setOptions(List<VoteOptionDto.RequestDto> options) { this.options = options; }
    }

    public static class ResponseDto {
        private Long id;
        private String name;
        private String author;
        private String category;
        private byte[] imagedata;

        private List<VoteOptionDto.ResponseDto> options;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public byte[] getImagedata() { return imagedata; }
        public void setImagedata(byte[] imagedata) { this.imagedata = imagedata; }

        public List<VoteOptionDto.ResponseDto> getOptions() { return options; }
        public void setOptions(List<VoteOptionDto.ResponseDto> options) { this.options = options; }
    }
}
