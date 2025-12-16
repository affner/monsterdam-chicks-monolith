import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, byteSize, getPaginationState, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './single-photo.reducer';

export const SinglePhoto = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const singlePhotoList = useAppSelector(state => state.singlePhoto.entities);
  const loading = useAppSelector(state => state.singlePhoto.loading);
  const totalItems = useAppSelector(state => state.singlePhoto.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="single-photo-heading" data-cy="SinglePhotoHeading">
        <Translate contentKey="monsterdamChicksApp.singlePhoto.home.title">Single Photos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.singlePhoto.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/single-photo/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.singlePhoto.home.createLabel">Create new Single Photo</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {singlePhotoList && singlePhotoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('thumbnail')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.thumbnail">Thumbnail</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnail')} />
                </th>
                <th className="hand" onClick={sort('thumbnailS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.thumbnailS3Key">Thumbnail S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thumbnailS3Key')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('contentS3Key')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.contentS3Key">Content S 3 Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contentS3Key')} />
                </th>
                <th className="hand" onClick={sort('likeCount')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.likeCount">Like Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('likeCount')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.contentPackage">Content Package</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.singlePhoto.creator">Creator</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {singlePhotoList.map((singlePhoto, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/single-photo/${singlePhoto.id}`} color="link" size="sm">
                      {singlePhoto.id}
                    </Button>
                  </td>
                  <td>
                    {singlePhoto.thumbnail ? (
                      <div>
                        {singlePhoto.thumbnailContentType ? (
                          <a onClick={openFile(singlePhoto.thumbnailContentType, singlePhoto.thumbnail)}>
                            <img
                              src={`data:${singlePhoto.thumbnailContentType};base64,${singlePhoto.thumbnail}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {singlePhoto.thumbnailContentType}, {byteSize(singlePhoto.thumbnail)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{singlePhoto.thumbnailS3Key}</td>
                  <td>
                    {singlePhoto.content ? (
                      <div>
                        {singlePhoto.contentContentType ? (
                          <a onClick={openFile(singlePhoto.contentContentType, singlePhoto.content)}>
                            <img
                              src={`data:${singlePhoto.contentContentType};base64,${singlePhoto.content}`}
                              style={{ maxHeight: '30px' }}
                            />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {singlePhoto.contentContentType}, {byteSize(singlePhoto.content)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{singlePhoto.contentS3Key}</td>
                  <td>{singlePhoto.likeCount}</td>
                  <td>
                    {singlePhoto.createdDate ? <TextFormat type="date" value={singlePhoto.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {singlePhoto.lastModifiedDate ? (
                      <TextFormat type="date" value={singlePhoto.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{singlePhoto.createdBy}</td>
                  <td>{singlePhoto.lastModifiedBy}</td>
                  <td>
                    {singlePhoto.deletedDate ? <TextFormat type="date" value={singlePhoto.deletedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {singlePhoto.contentPackage ? (
                      <Link to={`/content-package/${singlePhoto.contentPackage.id}`}>{singlePhoto.contentPackage.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{singlePhoto.creator ? <Link to={`/user-lite/${singlePhoto.creator.id}`}>{singlePhoto.creator.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/single-photo/${singlePhoto.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/single-photo/${singlePhoto.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/single-photo/${singlePhoto.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.singlePhoto.home.notFound">No Single Photos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={singlePhotoList && singlePhotoList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default SinglePhoto;
