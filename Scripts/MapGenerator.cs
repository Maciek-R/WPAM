using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;

public class MapGenerator : MonoBehaviour {

    public enum DrawMode { NoiseMap, ColourMap, BluredColourMap};
    public DrawMode drawMode;

    public int width;
    public int height;
    public float noiseScale;

    public int octaves;
    [Range(0,1)]
    public float persistance;
    public float lacunarity;

    public int seed;
    public Vector2 offset;

    public bool autoUpdate;
    [Range(1, 30)]
    public int blurNumber;
  
    public TerrainType[] regions;

    private Texture2D texture;

    public void GenerateMap()
    {
        float[,] noiseMap = Noise.GenerateNoiseMap(width, height, seed, noiseScale, octaves, persistance, lacunarity, offset);

        Color[] colourMap = new Color[width * height];
        for(int y=0; y<height; ++y)
            for (int x = 0; x < width; ++x)
            {
                float currHeight = noiseMap[x, y];
                for(int i=0; i<regions.Length; ++i)
                    if(currHeight <= regions[i].height)
                    {
                        colourMap[y * width + x] = regions[i].colour;
                        break;
                    }
            }

        MapDisplay display = FindObjectOfType<MapDisplay>();

        if (DrawMode.NoiseMap.Equals(drawMode))
        {
            texture = TextureGenerator.GetTextureFromHeightMap(noiseMap);
            display.DrawTexture(texture);
        }      
        else if (DrawMode.ColourMap.Equals(drawMode))
        {
            texture = TextureGenerator.GetTextureFromColourMap(colourMap, width, height);
            display.DrawTexture(texture);
        }
        else if (DrawMode.BluredColourMap.Equals(drawMode))
        {
            Color[] bluredColourMap = BlurMap.GetBluredColourMap(colourMap, width, height);
            for (int i=0; i<blurNumber-1; ++i)
                bluredColourMap = BlurMap.GetBluredColourMap(bluredColourMap, width, height);
            
            texture = TextureGenerator.GetTextureFromColourMap(bluredColourMap, width, height);
            display.DrawTexture(texture);
        }
    }

    public void SaveMapAsTexture(string path)
    {
        string fileName="";
        if (DrawMode.NoiseMap.Equals(drawMode))
            fileName = "heightmap.png";
        else if (DrawMode.ColourMap.Equals(drawMode))
            fileName = "colourmap.png";
        else if (DrawMode.BluredColourMap.Equals(drawMode))
            fileName = "bluredcolourmap.png";

        var outFile = new FileInfo(path+"/"+fileName);
        outFile.Directory.Create();
        File.WriteAllBytes(outFile.FullName, texture.EncodeToPNG());
    }

    void OnValidate()
    {
        if (width < 1)
            width = 1;
        if (height < 1)
            height = 1;
        if (lacunarity < 1)
            lacunarity = 1;
        if (octaves < 0)
            octaves = 0;
        if (noiseScale < 0.001f)
            noiseScale = 0.001f;
    }
}

[System.Serializable]
public class TerrainType
{
    public string name;
    public float height;
    public Color colour;
}