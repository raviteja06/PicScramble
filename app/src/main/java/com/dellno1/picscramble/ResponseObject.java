package com.dellno1.picscramble;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ResponseObject implements Parcelable {
    @Expose
    private String title;
    @Expose
    private String link;
    @Expose
    private String description;
    @Expose
    private String modified;
    @Expose
    private String generator;
    @Expose
    private List<ItemObject> items = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public List<ItemObject> getItems() {
        return items;
    }

    public void setItems(List<ItemObject> items) {
        this.items = items;
    }

    public class ItemObject {
        @Expose
        private String title;
        @Expose
        private String link;
        @Expose
        private String date_taken;
        @Expose
        private String description;
        @Expose
        private String published;
        @Expose
        private String author;
        @Expose
        private String author_id;
        @Expose
        private String tags;
        @Expose
        private Media media;

        @Expose
        private boolean isFlipped;

        public boolean isFlipped() {
            return isFlipped;
        }

        public void setFlipped(boolean flipped) {
            isFlipped = flipped;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getDate_taken() {
            return date_taken;
        }

        public void setDate_taken(String date_taken) {
            this.date_taken = date_taken;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public Media getMedia() {
            return media;
        }

        public void setMedia(Media media) {
            this.media = media;
        }

        public class Media implements Parcelable {
            @Expose
            private String m;

            public String getM() {
                return m;
            }

            public void setM(String m) {
                this.m = m;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.m);
            }

            protected Media(Parcel in) {
                this.m = in.readString();
            }

            public final Creator<Media> CREATOR = new Creator<Media>() {
                public Media createFromParcel(Parcel source) {
                    return new Media(source);
                }

                public Media[] newArray(int size) {
                    return new Media[size];
                }
            };
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeString(this.modified);
        dest.writeString(this.generator);
        dest.writeList(this.items);
    }

    protected ResponseObject(Parcel in) {
        this.title = in.readString();
        this.link = in.readString();
        this.description = in.readString();
        this.modified = in.readString();
        this.generator = in.readString();
        in.readList(this.items, List.class.getClassLoader());
    }

    public static final Creator<ResponseObject> CREATOR = new Creator<ResponseObject>() {
        public ResponseObject createFromParcel(Parcel source) {
            return new ResponseObject(source);
        }

        public ResponseObject[] newArray(int size) {
            return new ResponseObject[size];
        }
    };
}