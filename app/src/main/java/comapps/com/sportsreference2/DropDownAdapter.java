package comapps.com.sportsreference2;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by me on 8/28/2017.
 */

class DropDownAdapter extends ArrayAdapter<SportsItem> {

    public DropDownAdapter(Activity context, int resouceId, int textviewId, List<SportsItem> list) {

        super(context, resouceId, textviewId, list);
//        inflater = context.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        SportsItem sportsItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = inflater != null ? inflater.inflate(R.layout.itemlayouthistory, null, false) : null;

            holder.txtName = rowview.findViewById(R.id.textViewName);
            holder.txtSeasons = rowview.findViewById(R.id.textViewSeasons);
            holder.txtType = rowview.findViewById(R.id.textViewType);
       //     holder.imageView = rowview.findViewById(R.id.imageViewIcon);
            rowview.setTag(holder);
        } else {

            holder = (viewHolder) rowview.getTag();

        }
        // holder.imageView.setImageResource(sportsItem.getImageId());
        holder.txtName.setText(sportsItem != null ? sportsItem.getName() : null);
        holder.txtSeasons.setText(sportsItem.getSeasons());
        holder.txtType.setText(sportsItem.getType());
        holder.imageView.setImageResource(R.drawable.baseball_icon_new2);

        return rowview;
    }

    private class viewHolder {
        TextView txtName;
        TextView txtSeasons;
        TextView txtType;
        ImageView imageView;
    }
}