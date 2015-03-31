package com.tsoft.myprocad.j3d;

import com.sun.j3d.loaders.ParsingErrorException;

import javax.vecmath.Color3f;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;

public class ObjectFileMaterials {
    private HashMap<String, Material3D> materials;
    private String curName = null;
    private Material3D cur = null;

    public void readMaterials(String str, HashMap<String, Material3D> materials) {
        this.materials = materials;

        Reader reader = new StringReader(str);
        ObjectFileParser st = new ObjectFileParser(reader);

        readFile(st);
    }

    private void readFile(ObjectFileParser st) throws ParsingErrorException {
        st.getToken();
        while (st.ttype != ObjectFileParser.TT_EOF) {
            if (st.ttype == ObjectFileParser.TT_WORD) {
                if (st.sval.equals("newmtl")) {
                    readName(st);
                } else if (st.sval.equals("ka")) {
                    readAmbient(st);
                } else if (st.sval.equals("kd")) {
                    readDiffuse(st);
                } else if (st.sval.equals("ks")) {
                    readSpecular(st);
                } else if (st.sval.equals("illum")) {
                    readIllum(st);
                } else if (st.sval.equals("d")) {
                    readTransparency(st);
                } else if (st.sval.equals("ns")) {
                    readShininess(st);
                } else if (st.sval.equals("tf")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("sharpness")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("map_kd")) {
                    readMapKd(st);
                } else if (st.sval.equals("map_ka")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("map_ks")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("map_ns")) {
                    st.skipToNextLine();
                } else if (st.sval.equals("bump")) {
                    st.skipToNextLine();
                }
            }

            st.skipToNextLine();

            // Get next token
            st.getToken();
        }

        if (curName != null) materials.put(curName, cur);
    }

    private void readName(ObjectFileParser st) throws ParsingErrorException {
        st.getToken();

        if (st.ttype == ObjectFileParser.TT_WORD) {
            if (curName != null) materials.put(curName, cur);
            curName = new String(st.sval);
            cur = new Material3D();
        }
        st.skipToNextLine();
    }

    private void readAmbient(ObjectFileParser st) throws ParsingErrorException {
        Color3f p = new Color3f();
        st.getNumber();
        p.x = (float) st.nval;
        st.getNumber();
        p.y = (float) st.nval;
        st.getNumber();
        p.z = (float) st.nval;
        cur.Ka = p;

        st.skipToNextLine();
    }

    private void readDiffuse(ObjectFileParser st) throws ParsingErrorException {
        Color3f p = new Color3f();
        st.getNumber();
        p.x = (float) st.nval;
        st.getNumber();
        p.y = (float) st.nval;
        st.getNumber();
        p.z = (float) st.nval;
        cur.Kd = p;

        st.skipToNextLine();
    }

    private void readSpecular(ObjectFileParser st) throws ParsingErrorException {
        Color3f p = new Color3f();
        st.getNumber();
        p.x = (float) st.nval;
        st.getNumber();
        p.y = (float) st.nval;
        st.getNumber();
        p.z = (float) st.nval;
        cur.Ks = p;

        st.skipToNextLine();
    }

    private void readIllum(ObjectFileParser st) throws ParsingErrorException {
        st.getNumber();
        cur.illum = (int) st.nval;

        st.skipToNextLine();
    }

    private void readTransparency(ObjectFileParser st) throws ParsingErrorException {
        st.getNumber();
        cur.transparencyLevel = (float) st.nval;
        if (cur.transparencyLevel < 1.0f) {
            cur.transparent = true;
        }
        st.skipToNextLine();
    }

    private void readShininess(ObjectFileParser st) throws ParsingErrorException {
        st.getNumber();
        cur.Ns = (float) st.nval;
        if (cur.Ns < 1.0f) cur.Ns = 1.0f;
        else if (cur.Ns > 128.0f) cur.Ns = 128.0f;

        st.skipToNextLine();
    }

    public void readMapKd(ObjectFileParser st) {
        // Filenames are case sensitive
        st.lowerCaseMode(false);

        // Get name of texture file (skip path)
        String tFile = null;
        do {
            st.getToken();
            if (st.ttype == ObjectFileParser.TT_WORD) tFile = st.sval;
        } while (st.ttype != ObjectFileParser.TT_EOL);

        st.lowerCaseMode(true);
/*
            if (tFile != null) {
                // Check for filename with no extension
                if (tFile.lastIndexOf('.') != -1) {
                    try {
                        // Convert filename to lower case for extension comparisons
                        String suffix = tFile.substring(tFile.lastIndexOf('.') + 1).toLowerCase();

                        TextureLoader t = null;
                        if ((suffix.equals("int")) || (suffix.equals("inta")) ||
                                (suffix.equals("rgb")) || (suffix.equals("rgba")) ||
                                (suffix.equals("bw")) || (suffix.equals("sgi"))) {
                            RgbFile f;
                            if (fromUrl) {
                                f = new RgbFile(new URL(basePath + tFile).openStream());
                            } else {
                                f = new RgbFile(new FileInputStream(basePath + tFile));
                            }
                            BufferedImage bi = f.getImage();

                            boolean luminance = suffix.equals("int") || suffix.equals("inta");
                            boolean alpha = suffix.equals("inta") || suffix.equals("rgba");
                            cur.transparent = alpha;

                            String s = null;
                            if (luminance && alpha) s = "LUM8_ALPHA8";
                            else if (luminance) s = "LUMINANCE";
                            else if (alpha) s = "RGBA";
                            else s = "RGB";

                            t = new TextureLoader(bi, s, TextureLoader.GENERATE_MIPMAP);
                        } else {
                            // For all other file types, use the TextureLoader
                            if (fromUrl) {
                                t = new TextureLoader(new URL(basePath + tFile), "RGB",
                                        TextureLoader.GENERATE_MIPMAP, null);
                            } else {
                                t = new TextureLoader(basePath + tFile, "RGB",
                                        TextureLoader.GENERATE_MIPMAP, null);
                            }
                        }

                        Texture2D texture = (Texture2D) t.getTexture();
                        if (texture != null) cur.t = texture;
                    } catch (FileNotFoundException e) {
                        // Texture won't get loaded if file can't be found
                    } catch (MalformedURLException e) {
                        // Texture won't get loaded if file can't be found
                    } catch (IOException e) {
                        // Texture won't get loaded if file can't be found
                    } catch (ImageException iex) {
                        // Texture won't get loaded if other problem
                    }
                }
            }
            */
        st.skipToNextLine();
    }
}
