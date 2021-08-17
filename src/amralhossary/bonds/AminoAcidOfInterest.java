package amralhossary.bonds;

import java.util.ArrayList;
import java.util.List;

//import javax.vecmath.Point3d;

import org.biojava.nbio.structure.AminoAcid;
import org.biojava.nbio.structure.AminoAcidImpl;
import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Element;

/**
 * 
 * 
 * @author Amr
 *
 */
public class AminoAcidOfInterest extends AminoAcidImpl implements GroupOfInterest{
	private static final long serialVersionUID = -6254684424281007523L;
	
//	private Atom[] positiveKeyAtoms=null;
//	private Atom[] negativeKeyAtoms=null;
	private ArrayList<ContainingCube> containingCubes=new ArrayList<AminoAcidOfInterest.ContainingCube>();
	
	protected int aAOfInterestType;

	protected int missingAtoms;
	protected String suffix="";
	private Atom[] keyAtoms;
	private Atom[] keyOAtoms = null;
	private Atom[] keyCAtoms = null;
	private Atom[] keyNAtoms = null;

	static int processedAtoms;

	public static AminoAcidOfInterest newAcidOfInterest(AminoAcid aminoAcidOfInterest){
		return new AminoAcidOfInterest(aminoAcidOfInterest);
	}
	
	public AminoAcidOfInterest(AminoAcid aminoAcidOfInterest) {
		super();
		//first stage: cloning
		this.setPDBFlag(aminoAcidOfInterest.has3D());		
		this.setResidueNumber(aminoAcidOfInterest.getResidueNumber());
		this.setPDBName(aminoAcidOfInterest.getPDBName());
		this.setAminoType(aminoAcidOfInterest.getAminoType());
		this.setRecordType(aminoAcidOfInterest.getRecordType());
		// copy the atoms
		for (Atom atom : aminoAcidOfInterest.getAtoms()) {
			this.addAtom(atom);
		}
		this.setChain(aminoAcidOfInterest.getChain());
		
		initialize();
	}

	private void initialize() {
		//second stage: setting code_type
		setCodeType();
		//third stage: filling contents
		fillContents();
		//forth stage
		putGroupOfInterestInCorrespondingCube(suffix);
	}

	protected void setCodeType() {
		// TODO replace this with enum
		String pdbName = getPDBName();
		if (GroupOfInterest.NAME_LYS.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_LYS;
//			ProteinParser.totalFoundLyc++;
		}else if (GroupOfInterest.NAME_ARG.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_ARG;
//			ProteinParser.totalFoundArg++;
		}else if (GroupOfInterest.NAME_HIS.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_HIS;
//			ProteinParser.totalFoundArg++;
		}else if (GroupOfInterest.NAME_GLU.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_GLU;
//			ProteinParser.totalFoundGlu++;
		}else if (GroupOfInterest.NAME_ASP.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_ASP;
//			ProteinParser.totalFoundAsp++;
		}else if (GroupOfInterest.NAME_GLN.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_GLN;
//			ProteinParser.totalFoundGln++;
		}else if (GroupOfInterest.NAME_ASN.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_ASN;
//			ProteinParser.totalFoundAsn++;
		}else if (GroupOfInterest.NAME_CYS.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_CYS;
//			ProteinParser.totalFoundCys++;
		}else if (GroupOfInterest.NAME_THR.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_THR;
//			ProteinParser.totalFoundCys++;
		}else if (GroupOfInterest.NAME_SER.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_SER;
//			ProteinParser.totalFoundCys++;
		}else if (GroupOfInterest.NAME_TYR.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_TYR;
//			ProteinParser.totalFoundCys++;
		}else if (GroupOfInterest.NAME_CSO.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_CSO;
//			ProteinParser.totalFoundCso++;
		}else if (GroupOfInterest.NAME_SEC.equals(pdbName) ) {
			this.aAOfInterestType=GroupOfInterest.CODE_SEC;
//			ProteinParser.totalFoundSec++;
		}else {
			this.aAOfInterestType=GroupOfInterest.CODE_OTHERS;
//			throw new IllegalArgumentException("Not of Interest");
		}
	}


	protected void fillContents() {
		Atom atom, od = null;
		switch (aAOfInterestType) {
		case GroupOfInterest.CODE_LYS:
			this.suffix="|"+GroupOfInterest.NAME_LYS;
			Atom nz=null;
			nz = getAtom("NZ");
			if (nz == null) {
				this.missingAtoms++;
				if(SettingsManager.debugging){
					System.out.println("ERROR: didn't find NZ in LYS ("+getResidueNumber()+")");
				}
			}
			keyAtoms= keyNAtoms = new Atom[] {nz};
			break;
		case GroupOfInterest.CODE_ARG:
			this.suffix="|"+GroupOfInterest.NAME_ARG;
			Atom ne=null;
			ne = getAtom("NE");
			if (ne == null) {
				this.missingAtoms++;
				if(SettingsManager.debugging){
					System.out.println("ERROR: didn't find NE in ARG ("+getResidueNumber()+")");
				}
			}
			Atom nh1=null;
			nh1 = getAtom("NH1");
			if (nh1 == null) {
				this.missingAtoms++;
				if(SettingsManager.debugging){
					System.out.println("ERROR: didn't find NH1 in ARG ("+getResidueNumber()+")");
				}
			}
			Atom nh2=null;
			nh2 = getAtom("NH2");
			if (nh2 == null) {
				this.missingAtoms++;
				if(SettingsManager.debugging){
					System.out.println("ERROR: didn't find NH2 in ARG ("+getResidueNumber()+")");
				}
			}
			if(ne != null || nh1 != null || nh2 != null)
				keyAtoms = keyNAtoms= new Atom[] {ne, nh1, nh2};
			break;
		case GroupOfInterest.CODE_HIS:
			this.suffix="|"+GroupOfInterest.NAME_HIS;
			Atom nd1=null;
			nd1 = getAtom("ND1");
			if (nd1 == null) {
				this.missingAtoms++;
				if(SettingsManager.debugging){
					System.out.println("ERROR: didn't find ND1 in ARG ("+getResidueNumber()+")");
				}
			}
			Atom ne2=null;
			ne2 = getAtom("NE2");
			if (ne2 == null) {
				this.missingAtoms++;
				if(SettingsManager.debugging){
					System.out.println("ERROR: didn't find NE2 in ARG ("+getResidueNumber()+")");
				}
			}
			if(nd1 != null || ne2 != null)
				keyAtoms = keyNAtoms= new Atom[] {nd1, ne2};
			break;
		case GroupOfInterest.CODE_GLU:
			this.suffix="|"+GroupOfInterest.NAME_GLU;
			atom = getAtom("CD");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = keyCAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_GLN:
			this.suffix="|"+GroupOfInterest.NAME_GLN;
			atom = getAtom("CD");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = keyCAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_ASP:
			this.suffix="|"+GroupOfInterest.NAME_ASP;
			atom = getAtom("CG");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = keyCAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_ASN:
			this.suffix="|"+GroupOfInterest.NAME_ASN;
			atom = getAtom("CG");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = keyCAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_CSO:
			this.suffix="|"+GroupOfInterest.NAME_CSO;
			atom = getAtom("OD");
			if (atom == null) {
				this.missingAtoms++;
			}
			od = atom;
			this.keyOAtoms = new Atom[]{od};
			atom = getAtom("SG");
			if (atom == null) {
				this.missingAtoms++;
			}
			if (od == null) {
				keyAtoms = new Atom[] {atom};
			} else {
				keyAtoms = new Atom[] {atom, od};
			}
			break;
		case GroupOfInterest.CODE_CYS:
			this.suffix="|"+GroupOfInterest.NAME_CYS;
			atom = getAtom("SG");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_SEC:
		case GroupOfInterest.CODE_SE7:
			this.suffix="|"+GroupOfInterest.NAME_SEC;
			atom = getAtom("SEG");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_THR:
			this.suffix = "|"+ GroupOfInterest.NAME_THR;
			atom = getAtom("OG1");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = keyOAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_SER:
			this.suffix = "|"+ GroupOfInterest.NAME_SER;
			atom = getAtom("OG");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyAtoms = keyOAtoms = new Atom[] {atom};
			break;
		case GroupOfInterest.CODE_TYR:
			this.suffix = "|"+ GroupOfInterest.NAME_TYR;
			atom = getAtom("OH");
			if (atom == null) {
				this.missingAtoms++;
			}
			keyOAtoms = new Atom[] {atom};
			Atom cb  = getAtom("CB");
			Atom cd1 = getAtom("CD1");
			Atom cd2 = getAtom("CD2");
			Atom ce1 = getAtom("CE1");
			Atom ce2 = getAtom("CE2");
			keyCAtoms = new Atom[] {cb, cd1, cd2, ce1, ce2};
			keyAtoms = new Atom[] {atom, cb, cd1, cd2, ce1, ce2};
			break;
		case GroupOfInterest.CODE_OTHERS:
			List<Atom> atoms = getAtoms();
			ArrayList<Atom> heavyAtomsOfInterest= new ArrayList<Atom>();
			ArrayList<Atom> cAtomsOfInterest= new ArrayList<Atom>();
			ArrayList<Atom> oAtomsOfInterest= new ArrayList<Atom>();
			ArrayList<Atom> nAtomsOfInterest= new ArrayList<Atom>();
			for (Atom a : atoms) {
				Element element = a.getElement();
				if (element.isHeavyAtom()) {
					heavyAtomsOfInterest.add(a);
					if (Element.C == element) {
						cAtomsOfInterest.add(a);
					}else if (Element.O == element) {
						oAtomsOfInterest.add(a);
					}if (Element.N == element) {
						nAtomsOfInterest.add(a);
					}
				}
			}
			keyAtoms = heavyAtomsOfInterest.toArray(new Atom[heavyAtomsOfInterest.size()]);
			keyCAtoms = cAtomsOfInterest.toArray(new Atom[cAtomsOfInterest.size()]);
			keyOAtoms = oAtomsOfInterest.toArray(new Atom[oAtomsOfInterest.size()]);
			keyNAtoms = nAtomsOfInterest.toArray(new Atom[nAtomsOfInterest.size()]);
//			keyAtoms = new Atom[] {getN(),getO(),getC(),getCA(),getCB()};
			break;
		default:
			throw new IllegalArgumentException("Not of Interest");
		}
	}

	/**
	 * This function just puts {@link AminoAcid} elements in their corresponding cubes only.
	 * <i>i.e. no more check is done</i>.<br>
	 * note that an {@link AminoAcid} may be put in more than one cube if different atoms 
	 * are located in different cubes.
	 * @param aa the {@link AminoAcid} instance to put into cube.
	 */
	protected void putGroupOfInterestInCorrespondingCube(String suffix) {
		int x,y,z;
		x=y=z=0;
		if(keyAtoms == null)
			return;
		for (Atom keyAtom : keyAtoms) {
			if (keyAtom != null) {
				processedAtoms++;
				x = ProteinParser.angstromToCube(keyAtom.getX());
				y = ProteinParser.angstromToCube(keyAtom.getY());
				z = ProteinParser.angstromToCube(keyAtom.getZ());
				ProteinParser.putInCube(this, x, y, z, suffix);
			}
		}
	}

	//----------------Accessor methods---------------------

	/**
	 * @param containingCubes the containingCubes to set
	 */
	@Override
	public void setContainingCubes(ArrayList<ContainingCube> containingCubes) {
		this.containingCubes = containingCubes;
	}
	
	/**
	 * @return the containingCubes
	 */
	@Override
	public ArrayList<ContainingCube> getContainingCubes() {
		return containingCubes;
	}
	

	@Override
	public int getGroupOfInterestType() {
		return getaAOfInterestType();
	}
	
	/**
	 * @return the aAOfInterestType
	 */
	public int getaAOfInterestType() {
		return aAOfInterestType;
	}

	/**
	 * @return the missingAtoms
	 */
	@Override
	public int getMissingAtoms() {
		return missingAtoms;
	}

	/**
	 * @return the suffix
	 */
	@Override
	public String getSuffix() {
		return suffix;
	}

	@Override
	public Atom[] getKeyAtoms() {
		return keyAtoms;
	}

	@Override
	public Atom[] getKeyOAtoms() {
		return keyOAtoms;
	}
	
	@Override
	public Atom[] getKeyNAtoms() {
		return keyNAtoms;
	}
	
	@Override
	public Atom[] getKeyCAtoms() {
		return keyCAtoms;
	}
}