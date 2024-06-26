package net.runelite.client.plugins.tscripts.sevices.localpathfinder;

import org.jetbrains.kotlin.gnu.trove.TIntIntHashMap;

import java.util.LinkedList;
import java.util.List;

public class BFSCache
{
    private final TIntIntHashMap cache = new TIntIntHashMap(200000);

    public boolean put(final int point, final int parent)
    {
        if(cache.contains(point))
            return false;
        cache.put(point, parent);
        return true;
    }

    public int get(final int position)
    {
        return cache.get(position);
    }

    public void clear()
    {
        cache.clear();
    }

    public int size()
    {
        return cache.size();
    }

    public List<Step> path(int pos)
    {
        int parent = get(pos);
        LinkedList<Step> path = new LinkedList<>();
        path.add(0, new Step(pos));
        while(parent != -1)
        {
            pos = parent;
            parent = get(pos);
            path.add(0, new Step(pos));
        }
        return path;
    }
}