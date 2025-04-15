package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class VoteOptionDto {

    public static class RequestDto {
        private String name;
        private byte[] imagedata;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public byte[] getImagedata() { return imagedata; }
        public void setImagedata(byte[] imagedata) { this.imagedata = imagedata; }
    }

    public static class ResponseDto {
        private Long id;
        private String name;
        private int count;
        private byte[] imagedata;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }

        public byte[] getImagedata() { return imagedata; }
        public void setImagedata(byte[] imagedata) { this.imagedata = imagedata; }
    }
}

