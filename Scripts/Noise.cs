using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public static class Noise{

	public static float[,] GenerateNoiseMap(int width, int height, int seed, float scale, int octaves, float persistance, float lacunarity, Vector2 offset)
    {
        if (scale <= 0)
            scale = 0.001f;

        System.Random random = new System.Random(seed);
        Vector2[] octaveOffsets = new Vector2[octaves];
        for(int i=0; i<octaves; ++i)
        {
            float offsetX = random.Next(-10000, 10000) + offset.x;
            float offsetY = random.Next(-10000, 10000) + offset.y;
            octaveOffsets[i] = new Vector2(offsetX, offsetY);
        }

        float maxNoiseHeight = float.MinValue;
        float minNoiseHeight = float.MaxValue;

        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        float[,] noiseMap = new float[width, height];
        for(int y=0; y<height; ++y)
            for(int x = 0; x<width; ++x)
            {
                float amplitude = 1;
                float frequency = 1;
                float noiseHeight = 1;

                for(int i=0; i<octaves; ++i)
                {
                    float sampleX = (x-halfWidth) / scale * frequency + octaveOffsets[i].x;
                    float sampleY = (y-halfHeight) / scale * frequency + octaveOffsets[i].y;

                    float perlinValue = Mathf.PerlinNoise(sampleX, sampleY)*2 - 1;
                    noiseHeight += perlinValue * amplitude;
                    amplitude *= persistance;
                    frequency *= lacunarity;
                }

                maxNoiseHeight = (noiseHeight > maxNoiseHeight) ? noiseHeight : maxNoiseHeight;
                minNoiseHeight = (noiseHeight < minNoiseHeight) ? noiseHeight : minNoiseHeight;
             
                noiseMap[x, y] = noiseHeight;
            }

        return getNormalizedNoiseMap(noiseMap, minNoiseHeight, maxNoiseHeight);
    }

    private static float[,] getNormalizedNoiseMap(float[,] noiseMap, float minNoiseHeight, float maxNoiseHeight)
    {
        int width = noiseMap.GetLength(0);
        int height = noiseMap.GetLength(1);

        for (int y = 0; y < height; ++y)
            for (int x = 0; x < width; ++x)
                noiseMap[x, y] = Mathf.InverseLerp(minNoiseHeight, maxNoiseHeight, noiseMap[x, y]);

        return noiseMap;
    }
}
