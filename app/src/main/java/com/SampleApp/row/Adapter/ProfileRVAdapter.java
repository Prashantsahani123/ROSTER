package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.SampleApp.row.Data.profiledata.AddressData;
import com.SampleApp.row.Data.profiledata.BusinessMemberDetails;
import com.SampleApp.row.Data.profiledata.DynamicFieldData;
import com.SampleApp.row.Data.profiledata.FamilyMemberData;
import com.SampleApp.row.Data.profiledata.PersonalMemberDetails;
import com.SampleApp.row.Data.profiledata.PhotoData;
import com.SampleApp.row.Data.profiledata.Separator;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.AddressHolder;
import com.SampleApp.row.holders.BusinessDetailHolder;
import com.SampleApp.row.holders.FamilyMemberDetailHolder;
import com.SampleApp.row.holders.PersonalDetailHolder;
import com.SampleApp.row.holders.PhotoHolder;
import com.SampleApp.row.holders.SeparatorHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by USER1 on 24-03-2017.
 */
public class ProfileRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int PERSONAL_TYPE = 1, BUSINESS_TYPE = 2, ADDRESS_TYPE = 3, FAMILY_TYPE = 4, SEPARATOR = 5, PHOTO = 6;
    Context context;
    ArrayList<Object> list;
    public static final String COLUMN_TYPE_EMAIL = "Email", COLUMN_TYPE_PHONE = "Contact", COLUMN_TYPE_DATE = "Date", COLUMN_TYPE_URL = "url", COLUMN_TYPE_GOLOACTION = "geo";

    public ProfileRVAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    private boolean editable = false;

    @Override
    public int getItemViewType(int position) {

        Object obj = list.get(position);
        if (obj instanceof PersonalMemberDetails) {
            return PERSONAL_TYPE;
        } else if (obj instanceof BusinessMemberDetails) {
            return BUSINESS_TYPE;
        } else if (obj instanceof AddressData) {
            return ADDRESS_TYPE;
        } else if (obj instanceof FamilyMemberData) {
            return FAMILY_TYPE;
        } else if (obj instanceof Separator) {
            return SEPARATOR;
        } else if (obj instanceof PhotoData) {
            return PHOTO;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case PERSONAL_TYPE:
                View pView = inflater.inflate(R.layout.profile_personal_details, parent, false);
                PersonalDetailHolder pHolder = new PersonalDetailHolder(pView);
                return pHolder;

            case BUSINESS_TYPE:
                View bView = inflater.inflate(R.layout.profile_bussiness_details, parent, false);
                BusinessDetailHolder bHolder = new BusinessDetailHolder(bView);
                return bHolder;

            case ADDRESS_TYPE:
                View aView = inflater.inflate(R.layout.profile_address, parent, false);
                AddressHolder aHolder = new AddressHolder(aView);
                return aHolder;

            case FAMILY_TYPE:
                View fView = inflater.inflate(R.layout.profile_family_member_layout, parent, false);
                FamilyMemberDetailHolder fHolder = new FamilyMemberDetailHolder(fView);
                return fHolder;

            case SEPARATOR:
                View sView = inflater.inflate(R.layout.separator_layout, parent, false);
                SeparatorHolder sHolder = new SeparatorHolder(sView);
                return sHolder;

            case PHOTO:
                View photoView = inflater.inflate(R.layout.photo_layout, parent, false);
                PhotoHolder photoHolder = new PhotoHolder(photoView);
                return photoHolder;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) return;

        int type = getItemViewType(position);
        switch (type) {
            case PERSONAL_TYPE:
                bindPersonalData(holder, position);
                break;

            case BUSINESS_TYPE:
                bindBusinessData(holder, position);
                break;

            case ADDRESS_TYPE:
                bindAddressData(holder, position);
                break;

            case FAMILY_TYPE:
                bindFamilyMemberData(holder, position);
                break;

            case SEPARATOR:
                bindSeparator(holder, position);
                break;

            case PHOTO:
                bindPhoto(holder, position);
                break;
        }
    }


    public String getFormattedDate(String date) {
        String[] dateFields = date.split("/");
        int month = Integer.parseInt(dateFields[1]);  // dateFileds[1] is month value
        String monthName = MONTHS[month];
        try {
            if (Integer.parseInt(dateFields[0]) == 0) {
                return "";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dateFields[0] + " " + monthName;
    }

    public void bindPersonalData(RecyclerView.ViewHolder holder, int position) {
        try {

            PersonalDetailHolder bHolder = (PersonalDetailHolder) holder;
            PersonalMemberDetails data = (PersonalMemberDetails) list.get(position);
            bHolder.getTvDynamicFieldTitle().setText(data.getKey());

            if(data.getKey()!=null && !data.getKey().trim().isEmpty()){
                bHolder.ll_item.setVisibility(View.VISIBLE);
                if (data.getColType().equalsIgnoreCase(COLUMN_TYPE_DATE)) {
                    // DD/MM/YYYY
                    String[] dateFields = data.getValue().split("/");
                    int month = Integer.parseInt(dateFields[1]);  // dateFileds[1] is month value
                    String monthName = MONTHS[month];
                    bHolder.getTvDynamicFieldValue().setText(dateFields[0] + " " + monthName);
                } else {
                    bHolder.getTvDynamicFieldValue().setText(data.getValue());
                }
                bindDynamicFieldAction(bHolder.getTvDynamicFieldValue(), data);
            }else {
                bHolder.ll_item.setVisibility(View.GONE);
            }


        } catch (ClassCastException ce) {
            Utils.log("Error is : " + ce);
            ce.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }

    public void bindBusinessData(RecyclerView.ViewHolder holder, int position) {
        try {

            BusinessDetailHolder bHolder = (BusinessDetailHolder) holder;
            BusinessMemberDetails data = (BusinessMemberDetails) list.get(position);
            if(data.getKey()!=null && !data.getKey().trim().isEmpty() && data.getValue()!=null && !data.getValue().trim().isEmpty()){
                bHolder.getTvDynamicFieldTitle().setText(data.getKey());
                bHolder.getTvDynamicFieldValue().setText(data.getValue());
                bindDynamicFieldAction(bHolder.getTvDynamicFieldValue(), data);
            }


        } catch (ClassCastException ce) {
            Utils.log("Error is : " + ce);
            ce.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }


    public void bindDynamicFieldAction(TextView tv, final DynamicFieldData data) {
        try {
            if (data.getColType().equalsIgnoreCase(COLUMN_TYPE_PHONE)) {
                tv.setTextColor(context.getResources().getColor(R.color.clickableBlueColor));
                tv.setTextSize(16);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.getValue()));
                        context.startActivity(intent);
                    }
                });
            } else if (data.getColType().equalsIgnoreCase(COLUMN_TYPE_EMAIL)) {

                tv.setTextColor(context.getResources().getColor(R.color.clickableBlueColor));
                tv.setTextSize(16);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setType("plain/text");
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{data.getValue()});

                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        context.startActivity(emailIntent);
                    }
                });


            } else if (data.getColType().equalsIgnoreCase(COLUMN_TYPE_URL)) {
                tv.setTextColor(context.getResources().getColor(R.color.clickableBlueColor));
                tv.setTextSize(16);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getValue()));
                        context.startActivity(intent);
                    }
                });

            } else if (data.getColType().equalsIgnoreCase(COLUMN_TYPE_GOLOACTION)) {
                tv.setTextColor(context.getResources().getColor(R.color.clickableBlueColor));
                tv.setTextSize(16);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("geo:" + data.getValue()));
                        context.startActivity(intent);
                    }
                });
            } else {
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(15.5f);
            }
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }

    public void bindAddressData(RecyclerView.ViewHolder holder, int position) {
        try {
            AddressHolder aHolder = (AddressHolder) holder;
            AddressData data = (AddressData) list.get(position);
            aHolder.getTvAddressTitle().setText(data.getAddressType() + " Address");

            ArrayList<String> valueList = new ArrayList<>();

            valueList.add(data.getAddress());
            valueList.add(data.getCity());
            valueList.add(data.getState());
            valueList.add(data.getCountry());
            valueList.add(data.getPincode());
            valueList.add(data.getFax());

            String address = Utils.implode(", ", valueList);
            aHolder.getTvAddressValue().setText(address);

        } catch (ClassCastException ce) {
            Utils.log("Error is : " + ce);
            ce.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }

    public void bindFamilyMemberData(RecyclerView.ViewHolder holder, int position) {
        try {
            FamilyMemberDetailHolder fHolder = (FamilyMemberDetailHolder) holder;
            final FamilyMemberData data = (FamilyMemberData) list.get(position);
            fHolder.getTvFamilyMemberRelation().setText(data.getRelationship());
            fHolder.getTvFamilyMemberName().setText(data.getMemberName());

            if (data.getContactNo().trim().equals("")) {
                holder.itemView.findViewById(R.id.ll_mobile).setVisibility(View.GONE);
            } else {
                holder.itemView.findViewById(R.id.ll_mobile).setVisibility(View.VISIBLE);
                fHolder.getTvFamilyMemberMobile().setText(data.getContactNo());
                fHolder.getTvFamilyMemberMobile().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + data.getContactNo()));
                        context.startActivity(callIntent);
                    }
                });
            }

            if (data.getEmailID().trim().equals("")) {
                holder.itemView.findViewById(R.id.ll_email).setVisibility(View.GONE);
            } else {
                holder.itemView.findViewById(R.id.ll_email).setVisibility(View.VISIBLE);
                fHolder.getTvFamilyMemberEmail().setText(data.getEmailID());

                fHolder.getTvFamilyMemberEmail().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setType("plain/text");
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{data.getEmailID().toString()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        context.startActivity(emailIntent);
                    }
                });

            }

            if (data.getDob().trim().equals("")) {
                holder.itemView.findViewById(R.id.ll_dob).setVisibility(View.GONE);
            } else {
                holder.itemView.findViewById(R.id.ll_dob).setVisibility(View.VISIBLE);
                String dob = getFormattedDate(data.getDob());
                fHolder.getTvFamilyMemberDob().setTag(data.getDob());
                fHolder.getTvFamilyMemberDob().setText(dob);
            }

            if (data.getAnniversary().trim().equals("")) {
                holder.itemView.findViewById(R.id.ll_anniversary).setVisibility(View.GONE);
            } else {
                holder.itemView.findViewById(R.id.ll_anniversary).setVisibility(View.VISIBLE);

                String anni = getFormattedDate(data.getAnniversary());
                if ( anni.equals("")) {
                    holder.itemView.findViewById(R.id.ll_anniversary).setVisibility(View.GONE);
                }
                fHolder.getTvFamilyMemberAnniversary().setTag(data.getAnniversary());
                fHolder.getTvFamilyMemberAnniversary().setText(anni);
            }

            if (data.getBloodGroup().trim().equals("")) {
                holder.itemView.findViewById(R.id.ll_blood).setVisibility(View.GONE);
            } else {
                holder.itemView.findViewById(R.id.ll_blood).setVisibility(View.VISIBLE);
                fHolder.getTvFamilyMemberBloodGroup().setText(data.getBloodGroup());
            }
        } catch (ClassCastException ce) {
            Utils.log("Error is : " + ce);
            ce.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

    }

    public void bindSeparator(RecyclerView.ViewHolder holder, int position) {

    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void bindPhoto(RecyclerView.ViewHolder holder, final int position) {
        try {
            //Toast.makeText(context, "We are here", Toast.LENGTH_SHORT).show();
            final PhotoHolder photoholder = (PhotoHolder) holder;
            PhotoData data = (PhotoData) list.get(position);
            String familyPic = data.getFamilyPic();
            Utils.log("Family pic path : " + familyPic);
            if (familyPic.trim().length() == 0 || familyPic.equalsIgnoreCase("null") || familyPic.isEmpty()) {
               // photoholder.getImage().setVisibility(View.GONE);
                photoholder.getImage().setImageDrawable(context.getResources().getDrawable(R.drawable.profile_pic));
                photoholder.img_prg.setVisibility(View.GONE);
                if ( editable ) {
                    photoholder.getIvIcon().setImageDrawable(context.getResources().getDrawable(R.drawable.add_blue));
                } else {
                    photoholder.getIvIcon().setVisibility(View.GONE);
                }
            } else {
                try {
                    Picasso.with(context).load(data.getFamilyPic())
                           .into(photoholder.getImage(), new Callback() {
                        @Override
                        public void onSuccess() {
                            photoholder.img_prg.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            photoholder.img_prg.setVisibility(View.GONE);
                        }
                    });
                    if ( editable) {
                        photoholder.getImage().setVisibility(View.VISIBLE);
                        photoholder.getIvIcon().setImageDrawable(context.getResources().getDrawable(R.drawable.edit));
                    } else {
                        photoholder.getImage().setVisibility(View.VISIBLE);
                        photoholder.getIvIcon().setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            photoholder.getImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        onFamilyPicSelectedListener.onFamilyPicSelected(position);
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                    }
                }
            });

            if ( editable ) {

                photoholder.getIvIcon().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            onFamilyPicEditedListener.onFamilyPicEdited(position);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                    }
                });
            }
        } catch (ClassCastException cce) {
            cce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static final String[] MONTHS = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public void setOnFamilyPicSelectedListener(OnFamilyPicSelectedListener onFamilyPicSelectedListener) {
        this.onFamilyPicSelectedListener = onFamilyPicSelectedListener;
    }

    private OnFamilyPicSelectedListener onFamilyPicSelectedListener;

    public interface OnFamilyPicSelectedListener {
        void onFamilyPicSelected(int position);
    }

    public void setOnFamilyPicEditedListener(OnFamilyPicEditedListener onFamilyPicEditedListener) {
        this.onFamilyPicEditedListener = onFamilyPicEditedListener;
    }

    private OnFamilyPicEditedListener onFamilyPicEditedListener;

    public interface OnFamilyPicEditedListener {
        void onFamilyPicEdited(int position);
    }
}
