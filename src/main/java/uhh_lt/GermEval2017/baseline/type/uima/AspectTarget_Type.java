
/*******************************************************************************
 * Copyright 2016
 * FG Language Technology
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uhh_lt.GermEval2017.baseline.type.uima;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Mon Jul 28 09:50:42 CEST 2016
 * @generated */
public class AspectTarget_Type extends Annotation_Type {
  /** @generated
   * @return the generator for this desc.type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator =
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (AspectTarget_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = AspectTarget_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new AspectTarget(addr, AspectTarget_Type.this);
  			   AspectTarget_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new AspectTarget(addr, AspectTarget_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = AspectTarget.typeIndexID;
  /** @generated
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tu.darmstadt.lt.ner.types.AspectTarget");

  /** @generated */
  final Feature casFeat_AspectTargetType;
  /** @generated */
  final int     casFeatCode_AspectTargetType;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value
   */
  public String getAspectTargetType(int addr) {
        if (featOkTst && casFeat_AspectTargetType == null)
      jcas.throwFeatMissing("AspectTargetType", "de.tu.darmstadt.lt.ner.types.AspectTarget");
    return ll_cas.ll_getStringValue(addr, casFeatCode_AspectTargetType);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set
   */
  public void setAspectTargetType(int addr, String v) {
        if (featOkTst && casFeat_AspectTargetType == null)
      jcas.throwFeatMissing("AspectTargetType", "de.tu.darmstadt.lt.ner.types.AspectTarget");
    ll_cas.ll_setStringValue(addr, casFeatCode_AspectTargetType, v);}





  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type
	 */
  public AspectTarget_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_AspectTargetType = jcas.getRequiredFeatureDE(casType, "AspectTargetType", "uima.cas.String", featOkTst);
    casFeatCode_AspectTargetType  = (null == casFeat_AspectTargetType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AspectTargetType).getCode();

  }
}



    