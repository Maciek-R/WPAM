using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public static class BlurMap {

    public static Color[] GetBluredColourMap(Color[] colourMap, int width, int height)
    {
        Color[] bluredColourMap = new Color[width * height];

        for (int y = 0; y < height; ++y)
        {
            for (int x = 0; x < width; ++x)
            {
                Color mixedColour = GetMixedColourFromNeighbours(colourMap, width, height, x, y);
                bluredColourMap[y * width + x] = mixedColour;
            }
        }
        return bluredColourMap;
    }

    private static Color GetMixedColourFromNeighbours(Color[] colourMap, int width, int height, int x, int y)
    {
        int numberOfExistingNeighbours = 0;
        Color mixedColour = Color.black;
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                if (ExistsPointOnMap(x + j, y + i, width, height))
                {
                    ++numberOfExistingNeighbours;
                    mixedColour += colourMap[(y + i) * width + x + j];
                }
            }
        }
        return mixedColour/numberOfExistingNeighbours;
    }

    private static bool ExistsPointOnMap(int x, int y, int width, int height)
    {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;
        else
            return true;
    }
}
