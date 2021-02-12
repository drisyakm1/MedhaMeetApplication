package com.example.mymapapplication.models;


import android.os.Parcel;
        import android.os.Parcelable;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Company implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_description")
    @Expose
    private String companyDescription;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("company_image_url")
    @Expose
    private String companyImageUrl;
    @SerializedName("avg_rating")
    @Expose
    private Integer avgRating;
    public final static Parcelable.Creator<Company> CREATOR = new Creator<Company>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        public Company[] newArray(int size) {
            return (new Company[size]);
        }

    }
            ;

    protected Company(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.companyId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.companyName = ((String) in.readValue((String.class.getClassLoader())));
        this.companyDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.companyImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.avgRating = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Company() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCompanyImageUrl() {
        return companyImageUrl;
    }

    public void setCompanyImageUrl(String companyImageUrl) {
        this.companyImageUrl = companyImageUrl;
    }

    public Integer getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Integer avgRating) {
        this.avgRating = avgRating;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(companyId);
        dest.writeValue(companyName);
        dest.writeValue(companyDescription);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(companyImageUrl);
        dest.writeValue(avgRating);
    }

    public int describeContents() {
        return 0;
    }

}
